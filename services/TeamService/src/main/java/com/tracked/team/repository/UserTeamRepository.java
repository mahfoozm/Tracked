package com.tracked.team.repository;

import com.tracked.team.model.UserTeamTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public interface UserTeamRepository extends CrudRepository<UserTeamTable, Integer> {
    ArrayList<Integer> getTeamMembers(String teamId);
    ArrayList<Integer> getUsersTeams(String userId);

    List<UserTeamTable> findAllByTeamId(Integer teamId);

    List<UserTeamTable> findAllByUserId(Integer userId);
}
