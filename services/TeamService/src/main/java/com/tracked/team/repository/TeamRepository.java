package com.tracked.team.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tracked.team.model.Team;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer>{
    ArrayList<Integer> getTeamName(String teamId);
    ArrayList<Integer> getTeamId(Integer teamId);
    Date getCreationDate(Integer teamId);
    Date getLastUpdatedDate(Integer teamId);
}
