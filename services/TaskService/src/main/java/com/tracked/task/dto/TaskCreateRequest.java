package com.tracked.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskCreateRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer projectId;
    private Integer assigneeUserId;
    private LocalDate startDate;
    private LocalDate endDate;

}
