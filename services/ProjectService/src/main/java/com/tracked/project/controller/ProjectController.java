package com.tracked.project.controller;

import com.tracked.event.user.UserEvent;
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

    @GetMapping("")
    public List<Project> getAllProjects() {
        return projectService.allProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Integer id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/user/{id}")
    public List<Integer> getProjectsByUserId(@PathVariable Integer id) {
        return userProjectService.usersProjects(id);
    }

    @PostMapping("/{projectId}/user/{userId}")
    public void addUserToProject(@PathVariable Integer projectId, @PathVariable Integer userId) {
        this.projectService.addUserToProject(projectId, userId);
    }

    @GetMapping("/{id}/users")
    public List<UserEvent> getProjectsUsers(@PathVariable Integer id) {
        return userProjectService.projectMembers(id);
    }

    @PostMapping("")
    public void createProject(@RequestBody ProjectDto projectDto) {
        projectService.createProject(projectDto);
    }

}
