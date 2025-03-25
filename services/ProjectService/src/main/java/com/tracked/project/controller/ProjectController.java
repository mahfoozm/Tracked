package com.tracked.project.controller;

import com.tracked.project.dtos.ProjectDto;
import com.tracked.project.model.Project;
import com.tracked.project.service.ProjectService;
import com.tracked.project.service.UserProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/projects")
@RestController
public class ProjectController {
    private final ProjectService projectService;
    private final UserProjectService userProjectService;

    public ProjectController(ProjectService projectService, UserProjectService userProjectService) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Integer projectID) {
        return projectService.getProjectById(projectID);
    }

    @GetMapping("/{id}")
    public List<Integer> getProjectsByUserId(@PathVariable Integer userID) {
        return userProjectService.usersProjects(userID);
    }

    @GetMapping("/{id}")
    public List<Integer> getProjectsUsers(@PathVariable Integer projectID) {
        return userProjectService.projectMembers(projectID);
    }

    @PostMapping("/")
    public void createProject(@RequestBody ProjectDto projectDto) {
        Project newProject = projectService.createProject(projectDto);
    }
}
