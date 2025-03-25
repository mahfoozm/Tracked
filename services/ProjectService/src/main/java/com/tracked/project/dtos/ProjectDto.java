package com.tracked.project.dtos;

import java.util.ArrayList;

public class ProjectDto {
    private Integer id;
    private String name;
    private ArrayList<Integer> userProjectsList; // Used for getting all projects a user participates in
    private ArrayList<Integer> projectMembers; // List of members in a projects


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

    public ArrayList<Integer> getUserProjectsList(Integer userId) {
        return userProjectsList;
    }

    public void setUserProjectsList(ArrayList<Integer> userProjectsList) {
        this.userProjectsList = userProjectsList;
    }

    public ArrayList<Integer> getTeamMembers() {
        return projectMembers;
    }

    public void setProjectMembers(ArrayList<Integer> projectMembers) {
        this.projectMembers = projectMembers;
    }
}
