package com.tracked.project.repository;

import com.tracked.event.project.ProjectEvent;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.project.model.Project;
import com.tracked.project.model.UserProjectTable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectDatabasePopulator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDatabasePopulator.class);

    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final List<Project> initialProjects;
    private final List<UserProjectTable> initialUserProjectTables;
    private final KafkaTemplate<Integer, ProjectEvent> kafkaTemplate;

    @Autowired
    public ProjectDatabasePopulator(
        ProjectRepository projectRepository,
        UserProjectRepository userProjectRepository,
        KafkaTemplate<Integer, ProjectEvent> kafkaTemplate
    ) {
        this.projectRepository = projectRepository;
        this.userProjectRepository = userProjectRepository;
        Project project1 = Project.builder()
            .id(1)
            .name("Course Project")
            .build();
        this.initialProjects = new ArrayList<>();
        this.initialProjects.add(project1);
        UserProjectTable userProjectTable1 = UserProjectTable.builder()
            .id(1)
            .userId(1)
            .project(project1)
            .build();
        this.initialUserProjectTables = new ArrayList<>();
        this.initialUserProjectTables.add(userProjectTable1);
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void initDatabase() {
        logger.info("Populating projects in database");
        for (Project project : this.initialProjects) {
            if (!this.projectRepository.existsById(project.getId())) {
                logger.info("Creating project {}", project.getId());
                this.projectRepository.save(project);
                logger.info("Created project {}", project.getId());
                this.kafkaTemplate.send(
                    TrackedKafkaTopic.PROJECT_TOPIC,
                    project.getId(),
                    ProjectEvent.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .createdAt(project.getCreatedAt())
                        .updatedAt(project.getUpdatedAt())
                        .userIds(new ArrayList<>())
                        .build()
                );
                logger.info("Sent project {} to Kafka", project.getId());
            }
        }

        for (UserProjectTable userProjectTable : this.initialUserProjectTables) {
            if (!this.userProjectRepository.existsById(userProjectTable.getId())) {
                logger.info("Creating user-project {}", userProjectTable.getId());
                this.userProjectRepository.save(userProjectTable);
                logger.info("Created user-project {}", userProjectTable.getId());
                this.kafkaTemplate.send(
                    TrackedKafkaTopic.PROJECT_TOPIC,
                    userProjectTable.getProject().getId(),
                    ProjectEvent.builder()
                        .id(userProjectTable.getProject().getId())
                        .name(userProjectTable.getProject().getName())
                        .createdAt(userProjectTable.getProject().getCreatedAt())
                        .updatedAt(userProjectTable.getProject().getUpdatedAt())
                        .userIds(List.of(userProjectTable.getUserId()))
                        .build()
                );
                logger.info("Sent user-project {} to Kafka", userProjectTable.getId());
            }
        }
    }

}
