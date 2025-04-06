package com.tracked.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracked.event.user.UserEvent;
import com.tracked.project.config.TestSecurityConfig;
import com.tracked.project.dtos.ProjectDto;
import com.tracked.project.model.Project;
import com.tracked.project.service.ProjectService;
import com.tracked.project.service.UserProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserProjectService userProjectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project sampleProject;

    @BeforeEach
    void setUp() {
        sampleProject = Project.builder()
            .id(1)
            .name("Test Project")
            .description("Sample description")
            .createdAt(new Date())
            .updatedAt(new Date())
            .userProjectTables(Collections.emptyList())
            .build();
    }

    @Test
    void shouldReturnAllProjects() throws Exception {
        List<Project> projects = Collections.singletonList(sampleProject);
        Mockito.when(projectService.allProjects()).thenReturn(projects);

        mockMvc.perform(get("/projects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name").value("Test Project"));
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        Mockito.when(projectService.getProjectById(1)).thenReturn(sampleProject);

        mockMvc.perform(get("/projects/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void shouldReturnProjectsByUserId() throws Exception {
        Mockito.when(userProjectService.usersProjects(42)).thenReturn(List.of(sampleProject));

        mockMvc.perform(get("/projects/user/42"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldAddUserToProject() throws Exception {
        mockMvc.perform(post("/projects/5/user/99"))
            .andExpect(status().isOk());

        verify(projectService).addUserToProject(5, 99);
    }

    @Test
    void shouldReturnUsersInProject() throws Exception {
        UserEvent mockUser = new UserEvent();
        mockUser.setId(100);
        mockUser.setEmail("alice@gmail.com");

        Mockito.when(userProjectService.projectMembers(1)).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/projects/1/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(100))
            .andExpect(jsonPath("$[0].email").value("alice@gmail.com"));
    }

    @Test
    void shouldCreateProject() throws Exception {
        ProjectDto dto = new ProjectDto("New Project", "Description", new ArrayList<>());

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        verify(projectService).createProject(Mockito.any(ProjectDto.class));
    }
}
