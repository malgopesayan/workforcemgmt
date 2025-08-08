package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.model.ActivityLog;
import java.util.List;

public interface ActivityLogRepository {
    ActivityLog save(ActivityLog log);
    List<ActivityLog> findByTaskId(Long taskId);
}