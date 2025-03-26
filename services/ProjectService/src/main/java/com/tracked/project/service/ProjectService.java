package com.tracked.project.service;

import com.tracked.project.dtos.ProjectDto;
import com.tracked.project.model.Project;
import com.tracked.project.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

    public Project createProject(ProjectDto input) {
        Project project = Project.builder()
            .name(input.getName())
            .build();
        return project;
    }
}
