package com.tracked.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tracked.event.project.ProjectEventStore;
import com.tracked.event.task.TaskEvent;
import com.tracked.event.user.UserEventStore;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.task.dto.TaskCreateRequest;
import com.tracked.task.dto.TaskResponse;
import com.tracked.task.dto.TaskUpdateRequest;
import com.tracked.task.model.Task;
import com.tracked.task.repository.TaskRepository;
import com.tracked.task.repository.query.TaskQuerySpecification;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserEventStore userEventStore;

    private final ProjectEventStore projectEventStore;

    private final KafkaTemplate<Integer, TaskEvent> kafkaTemplate;

    @Autowired
    public TaskService(
        TaskRepository taskRepository,
        UserEventStore userEventStore,
        ProjectEventStore projectEventStore,
        @Qualifier("taskKafkaTemplate") KafkaTemplate<Integer, TaskEvent> kafkaTemplate
    ) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.userEventStore = userEventStore;
        this.projectEventStore = projectEventStore;
    }

    public TaskResponse findById(Integer id) {
        Task task = this.taskRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new TaskResponse(
            task,
            this.userEventStore.getUserEvent(task.getCreatorUserId()),
            this.userEventStore.getUserEvent(task.getAssigneeUserId()),
            this.projectEventStore.getProjectEvent(task.getProjectId())
        );
    }

    public List<TaskResponse> findTasks(
        Optional<Integer> projectId,
        Optional<Integer> assigneeUserId,
        Optional<Integer> creatorUserId,
        Optional<Task.Status> status
    ) {
        List<Task> tasks = this.taskRepository.findAll(
            TaskQuerySpecification.filterBy(
                projectId,
                assigneeUserId,
                creatorUserId,
                status
            )
        );
        return tasks.stream()
            .map(task -> new TaskResponse(
                task,
                this.userEventStore.getUserEvent(task.getCreatorUserId()),
                this.userEventStore.getUserEvent(task.getAssigneeUserId()),
                this.projectEventStore.getProjectEvent(task.getProjectId())
            ))
            .toList();
    }

    public TaskResponse createTask(TaskCreateRequest taskCreateRequest) {
        Task task = Task.builder()
            .name(taskCreateRequest.getName())
            .status(taskCreateRequest.getStatus())
            .startDate(taskCreateRequest.getStartDate())
            .endDate(taskCreateRequest.getEndDate())
            .projectId(taskCreateRequest.getProjectId())
            .creatorUserId(taskCreateRequest.getCreatorUserId())
            .assigneeUserId(taskCreateRequest.getAssigneeUserId())
            .build();

        // if (task.getCreatorUserId() != null && this.userEventStore.getUserEvent(task.getCreatorUserId()) == null) {
        //     throw new ResponseStatusException(
        //         HttpStatus.BAD_REQUEST,
        //         String.format("Creating user with id %s does not exist", task.getCreatorUserId())
        //     );
        // }
        // if (task.getAssigneeUserId() != null && this.userEventStore.getUserEvent(task.getAssigneeUserId()) == null) {
        //     throw new ResponseStatusException(
        //         HttpStatus.BAD_REQUEST,
        //         String.format("Assigning user with id %s does not exist", task.getAssigneeUserId())
        //     );
        // }
        // if (task.getProjectId() != null && this.projectEventStore.getProjectEvent(task.getProjectId()) == null) {
        //     throw new ResponseStatusException(
        //         HttpStatus.BAD_REQUEST,
        //         String.format("Project with id %s does not exist", task.getProjectId())
        //     );
        // }

        task = this.taskRepository.save(task);

        this.kafkaTemplate.send(
            TrackedKafkaTopic.TASK_TOPIC,
            task.getId(),
            task.toTaskEvent()
        );

        return new TaskResponse(
            task,
            this.userEventStore.getUserEvent(task.getCreatorUserId()),
            this.userEventStore.getUserEvent(task.getAssigneeUserId()),
            this.projectEventStore.getProjectEvent(task.getProjectId())
        );
    }

    public TaskResponse updateTask(Integer id, TaskUpdateRequest taskUpdateRequest) {
        Task task = this.taskRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (taskUpdateRequest.getStatus() != null) {
            task.setStatus(taskUpdateRequest.getStatus());
        }
        if (taskUpdateRequest.getAssigneeUserId() != null) {
            if (this.userEventStore.getUserEvent(taskUpdateRequest.getAssigneeUserId()) == null) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Assigning user with id %s does not exist", task.getAssigneeUserId())
                );
            }
            task.setAssigneeUserId(taskUpdateRequest.getAssigneeUserId());
        }
        this.taskRepository.save(task);

        this.kafkaTemplate.send(
            TrackedKafkaTopic.TASK_TOPIC,
            task.getId(),
            task.toTaskEvent()
        );

        return new TaskResponse(
            task,
            this.userEventStore.getUserEvent(task.getCreatorUserId()),
            this.userEventStore.getUserEvent(task.getAssigneeUserId()),
            this.projectEventStore.getProjectEvent(task.getProjectId())
        );
    }

}
