package com.tracked.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tracked.project.model.Project;

import java.util.ArrayList;
import java.util.Date;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer>{
}
