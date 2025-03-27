package com.tracked.project.service;

import com.tracked.event.user.UserEvent;
import com.tracked.event.user.UserEventStore;
import com.tracked.project.model.UserProjectTable;
import com.tracked.project.repository.UserProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final UserEventStore userEventStore;

    public UserProjectService(UserProjectRepository userProjectRepository, UserEventStore userEventStore) {
        this.userProjectRepository = userProjectRepository;
        this.userEventStore = userEventStore;
    }

    public List<UserEvent> projectMembers(Integer projectId) {
        List<Integer> members = new ArrayList<>();
        List<UserProjectTable> query = userProjectRepository.findAllByProjectId(projectId);
        for (int i = 0; i < query.size(); i++) {
            members.add(query.get(i).getUserId());
        }
        return members.stream()
            .map(this.userEventStore::getUserEvent)
            .collect(Collectors.toList());
    }

    public List<Integer> usersProjects(Integer userId) {
        List<Integer> teams = new ArrayList<>();
        List<UserProjectTable> query = userProjectRepository.findAllByUserId(userId);
        for (int i = 0; i < query.size(); i++) {
            teams.add(query.get(i).getProject().getId());
        }
        return teams;
    }
}
