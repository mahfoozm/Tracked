package com.tracked.task.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tracked.event.user.UserEventStore;
import com.tracked.task.dto.TaskCreateRequest;
import com.tracked.task.dto.TaskResponse;
import com.tracked.task.dto.TaskUpdateRequest;
import com.tracked.task.model.Task;
import com.tracked.task.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final UserEventStore userEventStore;

    @Autowired
    public TaskController(TaskService taskService, UserEventStore userEventStore) {
        this.taskService = taskService;
        this.userEventStore = userEventStore;
    }

    @PostMapping("")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskCreateRequest taskCreateRequest) {
        log.info("Received task creation request: {}", taskCreateRequest);
        try {
            return ResponseEntity.ok(this.taskService.createTask(taskCreateRequest));
        } catch (Exception e) {
            log.error("Error creating task: ", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(this.taskService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable(value = "id") Integer id, @RequestBody @Valid TaskUpdateRequest taskUpdateRequest) {
        return ResponseEntity.ok(this.taskService.updateTask(id, taskUpdateRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<TaskResponse>> getTasks(
        @RequestParam(value = "project_id", required = false) Optional<Integer> projectId,
        @RequestParam(value = "assignee_user_id", required = false) Optional<Integer> assigneeUserId,
        @RequestParam(value = "creator_user_id", required = false) Optional<Integer> creatorUserId,
        @RequestParam(value = "status", required = false) Optional<Task.Status> status
    ) {
        return ResponseEntity.ok(
            this.taskService.findTasks(
                projectId,
                assigneeUserId,
                creatorUserId,
                status
            )
        );
    }

    // Used for debugging kafka integration.
    // @GetMapping("/debug/users")
    // public ResponseEntity<Map<String, Object>> debugUsers() {
    //     Map<String, Object> debug = new HashMap<>();
    //     debug.put("users", userEventStore.getAllUserEvents());
    //     debug.put("userEventStoreClass", userEventStore.getClass().getName());
    //     return ResponseEntity.ok(debug);
    // }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
