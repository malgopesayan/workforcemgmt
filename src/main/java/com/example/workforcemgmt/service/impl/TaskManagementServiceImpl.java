package com.example.workforcemgmt.service.impl;



import com.example.workforcemgmt.common.exception.ResourceNotFoundException;
import com.example.workforcemgmt.dto.*;
import com.example.workforcemgmt.mapper.ManualTaskMapper; // <-- IMPORTANT: Import your new manual mapper
import com.example.workforcemgmt.model.ActivityLog;
import com.example.workforcemgmt.model.Comment;
import com.example.workforcemgmt.model.TaskManagement;
import com.example.workforcemgmt.model.enums.Priority;
import com.example.workforcemgmt.model.enums.Task;
import com.example.workforcemgmt.model.enums.TaskStatus;
import com.example.workforcemgmt.repository.ActivityLogRepository;
import com.example.workforcemgmt.repository.CommentRepository;
import com.example.workforcemgmt.repository.TaskRepository;
import com.example.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    // These are the only fields we need now
    private final TaskRepository taskRepository;
    private final ActivityLogRepository activityLogRepository;
    private final CommentRepository commentRepository;

    // The constructor is now simpler, without any mappers
    public TaskManagementServiceImpl(TaskRepository taskRepository, ActivityLogRepository activityLogRepository, CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.activityLogRepository = activityLogRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Convert the main task object using our manual mapper
        TaskManagementDto dto = ManualTaskMapper.taskModelToDto(task);

        // Fetch and add the activity history
        List<ActivityLog> logs = activityLogRepository.findByTaskId(id);
        logs.sort(Comparator.comparing(ActivityLog::getTimestamp));
        dto.setActivityHistory(ManualTaskMapper.activityLogModelListToDtoList(logs));

        // Fetch and add the comments
        List<Comment> comments = commentRepository.findByTaskId(id);
        comments.sort(Comparator.comparing(Comment::getTimestamp));
        dto.setComments(ManualTaskMapper.commentModelListToDtoList(comments));

        return dto;
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            newTask.setCreationTime(System.currentTimeMillis());

            TaskManagement savedTask = taskRepository.save(newTask);
            createdTasks.add(savedTask);
            logActivity(savedTask.getId(), "Task created and assigned to user " + savedTask.getAssigneeId());
        }
        // Convert the list of created tasks to DTOs
        return ManualTaskMapper.taskModelListToDtoList(createdTasks);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            if (item.getTaskStatus() != null && task.getStatus() != item.getTaskStatus()) {
                TaskStatus oldStatus = task.getStatus();
                task.setStatus(item.getTaskStatus());
                logActivity(task.getId(), "Status changed from " + oldStatus + " to " + item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return ManualTaskMapper.taskModelListToDtoList(updatedTasks);
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED && t.getStatus() != TaskStatus.CANCELLED)
                    .collect(Collectors.toList());

            if (!tasksOfType.isEmpty()) {
                TaskManagement taskToReassign = tasksOfType.get(0);
                Long oldAssigneeId = taskToReassign.getAssigneeId();
                taskToReassign.setAssigneeId(request.getAssigneeId());
                taskRepository.save(taskToReassign);
                logActivity(taskToReassign.getId(), "Task reassigned from user " + oldAssigneeId + " to " + request.getAssigneeId());

                for (int i = 1; i < tasksOfType.size(); i++) {
                    TaskManagement taskToCancel = tasksOfType.get(i);
                    taskToCancel.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToCancel);
                    logActivity(taskToCancel.getId(), "Task cancelled due to reassignment.");
                }
            } else {
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssigneeId(request.getAssigneeId());
                newTask.setStatus(TaskStatus.ASSIGNED);
                newTask.setCreationTime(System.currentTimeMillis());
                TaskManagement savedTask = taskRepository.save(newTask);
                logActivity(savedTask.getId(), "New task created via assignment by reference for user " + savedTask.getAssigneeId());
            }
        }
        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());
        long reqStart = request.getStartDate();
        long reqEnd = request.getEndDate();

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> {
                    if (task.getStatus() == TaskStatus.CANCELLED) {
                        return false;
                    }
                    boolean isActiveAndOpen = task.getStatus() != TaskStatus.COMPLETED;
                    if (!isActiveAndOpen) {
                        return false;
                    }
                    if (task.getCreationTime() == null) {
                        return false;
                    }
                    boolean startedInRange = task.getCreationTime() >= reqStart && task.getCreationTime() <= reqEnd;
                    boolean startedBeforeRangeAndIsOpen = task.getCreationTime() < reqStart;
                    return startedInRange || startedBeforeRangeAndIsOpen;
                })
                .collect(Collectors.toList());

        return ManualTaskMapper.taskModelListToDtoList(filteredTasks);
    }

    @Override
    public List<TaskManagementDto> updatePriority(UpdatePriorityRequest request) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for(UpdatePriorityRequest.RequestItem item : request.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            Priority oldPriority = task.getPriority();
            if (oldPriority != item.getPriority()) {
                task.setPriority(item.getPriority());
                updatedTasks.add(taskRepository.save(task));
                logActivity(task.getId(), "Priority changed from " + oldPriority + " to " + item.getPriority());
            }
        }
        return ManualTaskMapper.taskModelListToDtoList(updatedTasks);
    }

    @Override
    public List<TaskManagementDto> fetchByPriority(Priority priority) {
        List<TaskManagement> tasks = taskRepository.findByPriority(priority);
        return ManualTaskMapper.taskModelListToDtoList(tasks);
    }

    @Override
    public CommentDto addComment(Long taskId, AddCommentRequest request) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot add comment. Task not found with id: " + taskId));

        Comment newComment = new Comment(null, taskId, request.getAuthorId(), request.getText(), System.currentTimeMillis());
        Comment savedComment = commentRepository.save(newComment);
        logActivity(taskId, "Comment added by user " + request.getAuthorId());

        // Convert the saved comment to a DTO before returning
        return ManualTaskMapper.commentModelToDto(savedComment);
    }

    private void logActivity(Long taskId, String description) {
        ActivityLog log = new ActivityLog(null, taskId, description, System.currentTimeMillis());
        activityLogRepository.save(log);
    }
}