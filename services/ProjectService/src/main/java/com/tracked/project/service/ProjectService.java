package com.tracked.project.service;

import com.tracked.event.project.ProjectEvent;
import com.tracked.event.user.UserEvent;
import com.tracked.event.user.UserEventStore;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.project.dtos.ProjectDto;
import com.tracked.project.model.Project;
import com.tracked.project.model.UserProjectTable;
import com.tracked.project.repository.ProjectRepository;
import com.tracked.project.repository.UserProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserEventStore userEventStore;
    private KafkaTemplate<Integer, ProjectEvent> kafkaTemplate;

    @Autowired
    public ProjectService(
        ProjectRepository projectRepository,
        UserProjectRepository userProjectRepository,
        UserEventStore userEventStore,
        @Qualifier("projectKafkaTemplate") KafkaTemplate<Integer, ProjectEvent> kafkaTemplate
    ) {
        this.projectRepository = projectRepository;
        this.userProjectRepository = userProjectRepository;
        this.userEventStore = userEventStore;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Project> allProjects() {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll().forEach(projects::add);
        return projects;
    }

    public Project getProjectById(int projectID) {
        Optional<Project> project = projectRepository.findById(projectID);
        if (project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else {
            return project.get();
        }
    }

    public void addUserToProject(int projectID, int userID) {
        Project project = this.projectRepository
            .findById(projectID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (this.userEventStore.getUserEvent(userID) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }

        UserProjectTable userProjectTable = UserProjectTable.builder()
            .project(project)
            .userId(userID)
            .build();
        this.userProjectRepository.save(userProjectTable);

        project = this.projectRepository
            .findById(projectID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Stream<Integer> userIdStream = Stream.concat(
            project.getUserProjectTables().stream().map(UserProjectTable::getUserId),
            Stream.of(userID)
        );
        List<Integer> userIds = userIdStream.toList();

        this.kafkaTemplate.send(
            TrackedKafkaTopic.PROJECT_TOPIC,
            project.getId(),
            ProjectEvent.builder()
                .id(project.getId())
                .name(project.getName())
                .userIds(userIds)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build()
        );
    }

    public Project createProject(ProjectDto input) {
        Project project = Project.builder()
            .name(input.getName())
            .build();
        project = this.projectRepository.save(project);
        this.kafkaTemplate.send(
            TrackedKafkaTopic.PROJECT_TOPIC,
            project.getId(),
            ProjectEvent.builder()
                .id(project.getId())
                .name(project.getName())
                .userIds(new ArrayList<>())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build()
        );
        return project;
    }
}
