package com.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ElementCollection
    @CollectionTable(name = "task_dependencies", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "previous_task_id")
    private List<Integer> previousTaskIds = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "duration_days")
    private Integer durationDays;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Decision> decisions = new ArrayList<>();

    public Task() {
        this.createdAt = LocalDateTime.now();
        this.status = TaskStatus.TODO;
    }

    public Task(String title, String description, Project project) {
        this();
        this.title = title;
        this.description = description;
        this.project = project;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<Integer> getPreviousTaskIds() { return previousTaskIds; }
    public void setPreviousTaskIds(List<Integer> previousTaskIds) { this.previousTaskIds = previousTaskIds; }

    public List<User> getAssignees() { return assignees; }
    public void setAssignees(List<User> assignees) { this.assignees = assignees; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public List<Decision> getDecisions() { return decisions; }
    public void setDecisions(List<Decision> decisions) { this.decisions = decisions; }


    public void addAssignee(User user) {
        this.assignees.add(user);
    }

    public void removeAssignee(User user) {
        this.assignees.remove(user);
    }

    public void addPreviousTask(Integer taskId) {
        this.previousTaskIds.add(taskId);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTask(this);
    }

    public void addDecision(Decision decision) {
        this.decisions.add(decision);
        decision.setTask(this);
    }
}