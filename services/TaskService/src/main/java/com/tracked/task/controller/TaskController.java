package com.tracked.task.controller;

import com.tracked.task.dto.TaskCreateRequest;
import com.tracked.task.dto.TaskResponse;
import com.tracked.task.dto.TaskUpdateRequest;
import com.tracked.task.dto.TaskUpdateStatusRequest;
import com.tracked.task.model.Task;
import com.tracked.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskCreateRequest taskCreateRequest) {
        return ResponseEntity.ok(this.taskService.createTask(taskCreateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.taskService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer id, @RequestBody @Valid TaskUpdateRequest taskUpdateRequest) {
        return ResponseEntity.ok(this.taskService.updateTask(id, taskUpdateRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<TaskResponse>> getTasks(
        @RequestParam(value = "project_id", required = false) Optional<Integer> projectId,
        @RequestParam(value = "assignee_user_id", required = false) Optional<Integer> assigneeUserId,
        @RequestParam(value = "creator_user_id", required = false) Optional<Integer> creatorUserId,
        @RequestParam(required = false) Optional<Task.Status> status
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

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

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
