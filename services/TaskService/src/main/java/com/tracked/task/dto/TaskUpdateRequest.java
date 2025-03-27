package com.tracked.task.dto;

import com.tracked.task.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskUpdateRequest {

    private Task.Status status;
    private Integer assigneeUserId;

}
