package com.tracked.task.service;

import com.tracked.event.task.TaskEvent;
import com.tracked.event.user.UserEvent;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.task.dto.TaskCreateRequest;
import com.tracked.task.dto.TaskResponse;
import com.tracked.task.model.Task;
import com.tracked.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final KafkaTemplate<Integer, TaskEvent> kafkaTemplate;

    @Autowired
    public TaskService(
        TaskRepository taskRepository,
        KafkaTemplate<Integer, TaskEvent> kafkaTemplate
    ) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public TaskResponse findById(Integer id) {
        Task task = this.taskRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return TaskResponse.builder()
            .name(task.getName())
            .startDate(task.getStartDate())
            .endDate(task.getEndDate())
            .projectId(task.getProjectId())
            .assigneeId(task.getAssigneeUserId())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getUpdatedAt())
            .build();
    }

    public TaskResponse createTask(TaskCreateRequest taskCreateRequest) {
        Task task = Task.builder()
            .name(taskCreateRequest.getName())
            .startDate(taskCreateRequest.getStartDate())
            .endDate(taskCreateRequest.getEndDate())
            .projectId(taskCreateRequest.getProjectId())
            .assigneeUserId(taskCreateRequest.getAssigneeUserId())
            .build();

        // TODO: When the project and team services are functional, we should
        //       add checks here to ensure that the project ID and assignee ID
        //       actually exist in their services.

        task = this.taskRepository.save(task);

        this.kafkaTemplate.send(
            TrackedKafkaTopic.TASK_TOPIC,
            task.getId(),
            TaskEvent.builder()
                .name(task.getName())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .projectId(task.getProjectId())
                .assigneeUserId(task.getAssigneeUserId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build()
        );

        return TaskResponse.builder()
            .name(task.getName())
            .startDate(task.getStartDate())
            .endDate(task.getEndDate())
            .projectId(task.getProjectId())
            .assigneeId(task.getAssigneeUserId())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getUpdatedAt())
            .build();
    }

}
