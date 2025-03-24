package com.tracked.project.repository;

import com.tracked.project.model.UserProjectTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public interface UserProjectRepository extends CrudRepository<UserProjectTable, Integer> {
    ArrayList<Integer> getProjectMembers(String projectId);
    ArrayList<Integer> getUsersProjects(String userId);

    List<UserProjectTable> findAllByProjectId(Integer projectId);

    List<UserProjectTable> findAllByUserId(Integer userId);
}
