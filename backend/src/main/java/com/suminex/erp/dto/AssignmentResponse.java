package com.suminex.erp.dto;

import java.time.LocalDateTime;

public class AssignmentResponse {

    private Long id;
    private String title;
    private String description;
    private String subjectName;
    private LocalDateTime dueDate;
    private String filePath;

    public AssignmentResponse(Long id, String title, String description, String subjectName,
                              LocalDateTime dueDate, String filePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subjectName = subjectName;
        this.dueDate = dueDate;
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public String getFilePath() {
        return filePath;
    }
}