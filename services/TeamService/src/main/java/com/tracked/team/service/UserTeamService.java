package com.tracked.team.service;

import com.tracked.team.model.UserTeamTable;
import com.tracked.team.repository.TeamRepository;
import com.tracked.team.repository.UserTeamRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;

    public UserTeamService(UserTeamRepository userTeamRepository) {
        this.userTeamRepository = userTeamRepository;
    }

    public List<Integer> teamMembers(Integer teamId) {
        List<Integer> members = new ArrayList<>();
        List<UserTeamTable> query = userTeamRepository.findAllByTeamId(teamId);
        for (int i = 0; i < query.size(); i++) {
            members.add(query.get(i).getUserId());
        }
        return members;
    }

    public List<Integer> usersTeams(Integer userId) {
        List<Integer> teams = new ArrayList<>();
        List<UserTeamTable> query = userTeamRepository.findAllByUserId(userId);
        for (int i = 0; i < query.size(); i++) {
            teams.add(query.get(i).getTeamId());
        }
        return teams;
    }
}
