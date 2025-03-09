package com.tracked.team.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userTeamTable")
public class UserTeamTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id; // This will probably not get used

    @Column(nullable = false)
    private Integer teamId;

    @Column(nullable = false)
    private Integer userId;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
