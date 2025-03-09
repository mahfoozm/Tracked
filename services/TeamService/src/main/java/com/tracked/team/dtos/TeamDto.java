package com.tracked.team.dtos;

import java.util.ArrayList;

public class TeamDto {
    private Integer id;
    private String name;
    private ArrayList<Integer> userTeamsList; // Used for getting all teams a user joins
    private ArrayList<Integer> teamMembers; // List of members in a team


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getUserTeamsList(Integer userId) {
        return userTeamsList;
    }

    public void setUserTeamsList(ArrayList<Integer> userTeamsList) {
        this.userTeamsList = userTeamsList;
    }

    public ArrayList<Integer> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<Integer> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
