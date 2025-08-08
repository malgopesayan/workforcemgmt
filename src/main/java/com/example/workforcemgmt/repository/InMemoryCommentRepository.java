package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCommentRepository implements CommentRepository {

    private final Map<Long, List<Comment>> commentStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(idCounter.incrementAndGet());
        }
        commentStore.computeIfAbsent(comment.getTaskId(), k -> new ArrayList<>()).add(comment);
        return comment;
    }

    @Override
    public List<Comment> findByTaskId(Long taskId) {
        return commentStore.getOrDefault(taskId, Collections.emptyList());
    }
}