package com.tracked.task.dto;

import com.tracked.task.model.Task;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskUpdateStatusRequest {

    @NotNull
    private Task.Status status;

}
