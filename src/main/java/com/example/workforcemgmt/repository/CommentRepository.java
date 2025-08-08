package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.model.Comment;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findByTaskId(Long taskId);
}