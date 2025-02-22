package org.example;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id; // This can be changed to a String
    private String name;
    private Team team;
    private User assignee;
    private LocalDate startDate;
    private LocalDate endDate;

    public Task(int id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public Task(int id, String name, Team team, User assignee) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.assignee = assignee;
    }

    public Task(int id, String name, Team team, User assignee, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.assignee = assignee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}