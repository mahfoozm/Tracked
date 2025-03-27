package com.tracked.task;

import com.tracked.event.project.ProjectSharedConfigReference;
import com.tracked.event.task.TaskSharedConfigReference;
import com.tracked.event.user.UserSharedConfigReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TaskSharedConfigReference.class, UserSharedConfigReference.class, ProjectSharedConfigReference.class})
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
