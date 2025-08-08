package com.example.workforcemgmt.service;


import com.example.workforcemgmt.dto.*;
import com.example.workforcemgmt.model.enums.Priority;

import java.util.List;

public interface TaskManagementService {
    TaskManagementDto findTaskById(Long id);
    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    List<TaskManagementDto> updatePriority(UpdatePriorityRequest request);
    List<TaskManagementDto> fetchByPriority(Priority priority);
    CommentDto addComment(Long taskId, AddCommentRequest request);
}