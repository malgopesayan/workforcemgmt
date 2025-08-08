package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.model.ActivityLog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryActivityLogRepository implements ActivityLogRepository {

    private final Map<Long, List<ActivityLog>> logStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public ActivityLog save(ActivityLog log) {
        if (log.getId() == null) {
            log.setId(idCounter.incrementAndGet());
        }
        logStore.computeIfAbsent(log.getTaskId(), k -> new ArrayList<>()).add(log);
        return log;
    }

    @Override
    public List<ActivityLog> findByTaskId(Long taskId) {
        return logStore.getOrDefault(taskId, Collections.emptyList());
    }
}