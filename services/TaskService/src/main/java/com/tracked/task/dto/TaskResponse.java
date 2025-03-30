package com.tracked.task.dto;

import com.tracked.event.project.ProjectEvent;
import com.tracked.event.user.UserEvent;
import com.tracked.task.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskResponse {

    private Integer id;
    private String name;
    private ProjectEvent project;
    private UserEvent creator;
    private String description;
    private UserEvent assignee;
    private Task.Status status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public TaskResponse(Task task, UserEvent creator, UserEvent assignee, ProjectEvent project) {
        this.id = task.getId();
        this.name = task.getName();
        this.project = project;
        this.creator = creator;
        this.description = task.getDescription();
        this.assignee = assignee;
        this.status = task.getStatus();
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
    }

}
