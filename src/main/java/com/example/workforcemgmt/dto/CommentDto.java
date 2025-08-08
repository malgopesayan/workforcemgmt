package com.example.workforcemgmt.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDto {
    private Long id;
    private Long taskId;
    private Long authorId;
    private String text;
    private Long timestamp;
}