package com.tracked.event.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProjectEvent {

    private Integer id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private List<Integer> userIds;

}
