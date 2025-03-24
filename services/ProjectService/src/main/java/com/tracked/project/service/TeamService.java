package com.tracked.project.service;

import com.tracked.project.model.Project;
import com.tracked.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    private final ProjectRepository teamRepository;

    public TeamService(ProjectRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Project> allTeams() {
        List<Project> teams = new ArrayList<>();
        teamRepository.findAll().forEach(teams::add);
        return teams;
    }
}
