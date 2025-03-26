package com.tracked.task.repository;

import com.tracked.event.task.TaskEvent;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.task.model.Task;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskDatabasePopulator {

    private static final Logger logger = LoggerFactory.getLogger(TaskDatabasePopulator.class);

    private final TaskRepository taskRepository;
    private final List<Task> initialTasks;
    private final KafkaTemplate<Integer, TaskEvent> kafkaTemplate;

    @Autowired
    public TaskDatabasePopulator(
        TaskRepository taskRepository,
        KafkaTemplate<Integer, TaskEvent> kafkaTemplate
    ) {
        this.taskRepository = taskRepository;
        Task task1 = Task.builder()
            .id(1)
            .name("Finish Project")
            .projectId(1)
            .assigneeUserId(1)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .build();
        Task task2 = Task.builder()
            .id(2)
            .name("Finish Report")
            .projectId(1)
            .assigneeUserId(1)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .build();
        this.initialTasks = new ArrayList<>();
        this.initialTasks.add(task1);
        this.initialTasks.add(task2);
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void initDatabase() {
        logger.info("Populating tasks in database");
        for (Task task : this.initialTasks) {
            if (!this.taskRepository.existsById(task.getId())) {
                logger.info("Creating task {}", task.getId());
                this.taskRepository.save(task);
                logger.info("Created task {}", task.getId());
                this.kafkaTemplate.send(
                    TrackedKafkaTopic.TASK_TOPIC,
                    task.getId(),
                    new TaskEvent(
                        task.getId(),
                        task.getName(),
                        task.getProjectId(),
                        task.getAssigneeUserId(),
                        task.getStartDate(),
                        task.getEndDate(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                    )
                );
                logger.info("Sent task {} to Kafka", task.getId());
            }
        }
    }

}
