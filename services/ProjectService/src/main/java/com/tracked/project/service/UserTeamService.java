package com.tracked.project.service;

import com.tracked.project.model.UserProjectTable;
import com.tracked.project.repository.UserProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserTeamService {

    private final UserProjectRepository userTeamRepository;

    public UserTeamService(UserProjectRepository userTeamRepository) {
        this.userTeamRepository = userTeamRepository;
    }

    public List<Integer> teamMembers(Integer teamId) {
        List<Integer> members = new ArrayList<>();
        List<UserProjectTable> query = userTeamRepository.findAllByTeamId(teamId);
        for (int i = 0; i < query.size(); i++) {
            members.add(query.get(i).getUserId());
        }
        return members;
    }

    public List<Integer> usersTeams(Integer userId) {
        List<Integer> teams = new ArrayList<>();
        List<UserProjectTable> query = userTeamRepository.findAllByUserId(userId);
        for (int i = 0; i < query.size(); i++) {
            teams.add(query.get(i).getProjectId());
        }
        return teams;
    }
}
