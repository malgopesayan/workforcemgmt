package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.model.TaskManagement;
import com.example.workforcemgmt.model.enums.Priority;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<TaskManagement> findById(Long id);
    TaskManagement save(TaskManagement task);
    List<TaskManagement> findAll();
    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
    List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
    List<TaskManagement> findByPriority(Priority priority);
}