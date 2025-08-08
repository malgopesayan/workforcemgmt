package com.example.workforcemgmt.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    private Long id;
    private Long taskId;
    private String description;
    private Long timestamp;
}