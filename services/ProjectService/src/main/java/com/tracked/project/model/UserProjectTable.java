package com.tracked.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userProjectTable")
public class UserProjectTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id; // This will probably not get used

    @Column(nullable = false)
    private Integer projectId;

    @Column(nullable = false)
    private Integer userId;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
