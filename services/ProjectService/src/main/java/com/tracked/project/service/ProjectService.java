package com.tracked.project.service;

import com.tracked.project.model.Project;
import com.tracked.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> allProjects() {
        List<Project> teams = new ArrayList<>();
        projectRepository.findAll().forEach(teams::add);
        return teams;
    }
}
