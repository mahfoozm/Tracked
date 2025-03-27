package com.tracked.task.repository.query;

import com.tracked.task.model.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskQuerySpecification {

    public static Specification<Task> filterBy(
        Optional<Integer> projectId,
        Optional<Integer> assigneeUserId,
        Optional<Integer> creatorUserId,
        Optional<Task.Status> status
    ) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (projectId.isPresent()) {
                predicates.add(cb.equal(root.get("projectId"), projectId.get()));
            }
            if (assigneeUserId.isPresent()) {
                predicates.add(cb.equal(root.get("assigneeUserId"), assigneeUserId.get()));
            }
            if (creatorUserId.isPresent()) {
                predicates.add(cb.equal(root.get("creatorUserId"), creatorUserId.get()));
            }
            if (status.isPresent()) {
                predicates.add(cb.equal(root.get("status"), status.get()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
