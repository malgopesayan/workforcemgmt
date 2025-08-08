package com.example.workforcemgmt.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long id;
    private Long taskId;
    private Long authorId;
    private String text;
    private Long timestamp;
}