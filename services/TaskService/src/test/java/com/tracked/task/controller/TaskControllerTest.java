package com.tracked.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracked.task.config.TestSecurityConfig;
import com.tracked.task.dto.TaskCreateRequest;
import com.tracked.task.dto.TaskResponse;
import com.tracked.task.dto.TaskUpdateRequest;
import com.tracked.task.model.Task;
import com.tracked.task.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private com.tracked.event.user.UserEventStore userEventStore;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskResponse sampleResponse;

    @BeforeEach
    void setup() {
        sampleResponse = new TaskResponse();
    }

    @Test
    void testCreateTask() throws Exception {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setName("Test Task");
        request.setProjectId(1);
        request.setCreatorUserId(2);
        request.setAssigneeUserId(3);
        request.setStatus(Task.Status.NOT_STARTED);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(7));

        when(taskService.createTask(any())).thenReturn(sampleResponse);

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").doesNotExist()); // Adjust fields as needed
    }

    @Test
    void testGetTaskById() throws Exception {
        when(taskService.findById(1)).thenReturn(sampleResponse);

        mockMvc.perform(get("/task/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setStatus(Task.Status.DONE);

        when(taskService.updateTask(any(), any())).thenReturn(sampleResponse);

        mockMvc.perform(put("/task/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void testGetTasksWithFilters() throws Exception {
        when(taskService.findTasks(
            Optional.of(1),
            Optional.empty(),
            Optional.empty(),
            Optional.of(Task.Status.IN_PROGRESS)))
            .thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/task")
                .param("project_id", "1")
                .param("status", "IN_PROGRESS"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testValidationError() throws Exception {
        TaskCreateRequest badRequest = new TaskCreateRequest(); // missing required fields

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)))
            .andExpect(status().isBadRequest());
    }
}
