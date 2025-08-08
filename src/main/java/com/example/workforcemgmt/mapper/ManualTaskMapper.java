package com.example.workforcemgmt.mapper;


import com.example.workforcemgmt.dto.ActivityLogDto;
import com.example.workforcemgmt.dto.CommentDto;
import com.example.workforcemgmt.dto.TaskManagementDto;
import com.example.workforcemgmt.model.ActivityLog;
import com.example.workforcemgmt.model.Comment;
import com.example.workforcemgmt.model.TaskManagement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class for manually converting between Model and DTO objects.
 * This class replaces the need for the MapStruct library.
 */
public class ManualTaskMapper {

    // --- TaskManagement Mappers ---

    /**
     * Converts a TaskManagement model object into a TaskManagementDto.
     * @param model The TaskManagement object from the database/repository.
     * @return A TaskManagementDto object ready to be sent as an API response.
     */
    public static TaskManagementDto taskModelToDto(TaskManagement model) {
        if (model == null) {
            return null;
        }

        TaskManagementDto dto = new TaskManagementDto();

        // Manually copy every field from the model to the dto
        dto.setId(model.getId());
        dto.setReferenceId(model.getReferenceId());
        dto.setReferenceType(model.getReferenceType());
        dto.setTask(model.getTask());
        dto.setDescription(model.getDescription());
        dto.setStatus(model.getStatus());
        dto.setAssigneeId(model.getAssigneeId());
        dto.setTaskDeadlineTime(model.getTaskDeadlineTime());
        dto.setPriority(model.getPriority());
        dto.setCreationTime(model.getCreationTime());
        // History and comments will be added separately in the service layer.

        return dto;
    }

    /**
     * Converts a list of TaskManagement model objects into a list of TaskManagementDto objects.
     * @param models A list of TaskManagement objects.
     * @return A list of TaskManagementDto objects.
     */
    public static List<TaskManagementDto> taskModelListToDtoList(List<TaskManagement> models) {
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }
        return models.stream()
                .map(ManualTaskMapper::taskModelToDto)
                .collect(Collectors.toList());
    }


    // --- Comment Mappers ---

    /**
     * Converts a Comment model object into a CommentDto.
     * @param model The Comment object.
     * @return A CommentDto object.
     */
    public static CommentDto commentModelToDto(Comment model) {
        if (model == null) {
            return null;
        }

        CommentDto dto = new CommentDto();
        dto.setId(model.getId());
        dto.setTaskId(model.getTaskId());
        dto.setAuthorId(model.getAuthorId());
        dto.setText(model.getText());
        dto.setTimestamp(model.getTimestamp());
        return dto;
    }

    /**
     * Converts a list of Comment model objects into a list of CommentDto objects.
     * @param models A list of Comment objects.
     * @return A list of CommentDto objects.
     */
    public static List<CommentDto> commentModelListToDtoList(List<Comment> models) {
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }
        return models.stream()
                .map(ManualTaskMapper::commentModelToDto)
                .collect(Collectors.toList());
    }


    // --- ActivityLog Mappers ---

    /**
     * Converts an ActivityLog model object into an ActivityLogDto.
     * @param model The ActivityLog object.
     * @return An ActivityLogDto object.
     */
    public static ActivityLogDto activityLogModelToDto(ActivityLog model) {
        if (model == null) {
            return null;
        }

        ActivityLogDto dto = new ActivityLogDto();
        dto.setId(model.getId());
        dto.setTaskId(model.getTaskId());
        dto.setDescription(model.getDescription());
        dto.setTimestamp(model.getTimestamp());
        return dto;
    }

    /**
     * Converts a list of ActivityLog model objects into a list of ActivityLogDto objects.
     * @param models A list of ActivityLog objects.
     * @return A list of ActivityLogDto objects.
     */
    public static List<ActivityLogDto> activityLogModelListToDtoList(List<ActivityLog> models) {
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }
        return models.stream()
                .map(ManualTaskMapper::activityLogModelToDto)
                .collect(Collectors.toList());
    }
}