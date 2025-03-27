package com.tracked.task.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tracked.event.task.TaskEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    public enum Status {
        NOT_STARTED("NOT_STARTED"),
        IN_PROGRESS("IN_PROGRESS"),
        DONE("DONE"),
        CANCELLED("CANCELLED");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        public String fromValue() {
            return this.value;
        }

        @JsonCreator
        public static Status fromValue(String value) {
            for (Status status : Status.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid task status: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "project_id")
    private Integer projectId;

    @Column(nullable = false, name = "creator_user_id")
    private Integer creatorUserId;

    @Column(name = "assignee_user_id")
    private Integer assigneeUserId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public TaskEvent toTaskEvent() {
        return TaskEvent.builder()
            .id(this.getId())
            .name(this.getName())
            .creatorUserId(this.getCreatorUserId())
            .assigneeUserId(this.getAssigneeUserId())
            .status(this.getStatus().fromValue())
            .startDate(this.getStartDate())
            .endDate(this.getEndDate())
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }

}
