package com.tracked.project.service;

import com.tracked.project.model.UserProjectTable;
import com.tracked.project.repository.UserProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;

    public UserProjectService(UserProjectRepository userProjectRepository) {
        this.userProjectRepository = userProjectRepository;
    }

    public List<Integer> projectMembers(Integer teamId) {
        List<Integer> members = new ArrayList<>();
        List<UserProjectTable> query = userProjectRepository.findAllByProjectId(teamId);
        for (int i = 0; i < query.size(); i++) {
            members.add(query.get(i).getUserId());
        }
        return members;
    }

    public List<Integer> usersProjects(Integer userId) {
        List<Integer> teams = new ArrayList<>();
        List<UserProjectTable> query = userProjectRepository.findAllByUserId(userId);
        for (int i = 0; i < query.size(); i++) {
            teams.add(query.get(i).getProjectId());
        }
        return teams;
    }
}
