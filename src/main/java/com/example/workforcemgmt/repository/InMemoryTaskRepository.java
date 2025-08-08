package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.model.TaskManagement;
import com.example.workforcemgmt.model.enums.Priority;
import com.example.workforcemgmt.model.enums.Task;
import com.example.workforcemgmt.model.enums.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Long, TaskManagement> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public InMemoryTaskRepository(ActivityLogRepository activityLogRepository) {
        long now = System.currentTimeMillis();
        long oneDay = 86400000;
        createSeedTask(101L, ReferenceType.ORDER, Task.CREATE_INVOICE, 1L, TaskStatus.ASSIGNED, Priority.HIGH, now - (oneDay * 2), activityLogRepository);
        createSeedTask(101L, ReferenceType.ORDER, Task.ARRANGE_PICKUP, 1L, TaskStatus.COMPLETED, Priority.HIGH, now - (oneDay * 5), activityLogRepository);
        createSeedTask(102L, ReferenceType.ORDER, Task.CREATE_INVOICE, 2L, TaskStatus.ASSIGNED, Priority.MEDIUM, now - oneDay, activityLogRepository);
        createSeedTask(201L, ReferenceType.ENTITY, Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 2L, TaskStatus.ASSIGNED, Priority.LOW, now, activityLogRepository);
        createSeedTask(201L, ReferenceType.ENTITY, Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 3L, TaskStatus.ASSIGNED, Priority.LOW, now, activityLogRepository);
        createSeedTask(103L, ReferenceType.ORDER, Task.COLLECT_PAYMENT, 1L, TaskStatus.CANCELLED, Priority.MEDIUM, now - (oneDay * 3), activityLogRepository);
    }

    private void createSeedTask(Long refId, ReferenceType refType, Task task, Long assigneeId, TaskStatus status, Priority priority, long creationTime, ActivityLogRepository activityLogRepository) {
        long newId = idCounter.incrementAndGet();
        TaskManagement newTask = new TaskManagement();
        newTask.setId(newId);
        newTask.setReferenceId(refId);
        newTask.setReferenceType(refType);
        newTask.setTask(task);
        newTask.setAssigneeId(assigneeId);
        newTask.setStatus(status);
        newTask.setPriority(priority);
        newTask.setDescription("This is a seed task.");
        newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000);
        newTask.setCreationTime(creationTime);
        taskStore.put(newId, newTask);
        activityLogRepository.save(new com.example.workforcemgmt.model.ActivityLog(null, newId, "Seed task created.", creationTime));
    }

    @Override
    public Optional<TaskManagement> findById(Long id) {
        return Optional.ofNullable(taskStore.get(id));
    }

    @Override
    public TaskManagement save(TaskManagement task) {
        if (task.getId() == null) {
            task.setId(idCounter.incrementAndGet());
        }
        taskStore.put(task.getId(), task);
        return task;
    }

    @Override
    public List<TaskManagement> findAll() {
        return List.copyOf(taskStore.values());
    }

    @Override
    public List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType) {
        return taskStore.values().stream()
                .filter(task -> task.getReferenceId().equals(referenceId) && task.getReferenceType().equals(referenceType))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds) {
        return taskStore.values().stream()
                .filter(task -> assigneeIds.contains(task.getAssigneeId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagement> findByPriority(Priority priority) {
        return taskStore.values().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }
}