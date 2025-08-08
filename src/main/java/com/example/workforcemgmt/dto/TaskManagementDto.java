package com.example.workforcemgmt.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.model.enums.Priority;
import com.example.workforcemgmt.model.enums.Task;
import com.example.workforcemgmt.model.enums.TaskStatus;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskManagementDto {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    // ...continued from previous file
    private String description;
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    private Priority priority;
    private Long creationTime;
    private List<ActivityLogDto> activityHistory;
    private List<CommentDto> comments;
}