package com.suminex.erp.dto;

import java.time.LocalDateTime;

public class AuditLogResponse {

    private Long id;
    private String actorEmail;
    private String action;
    private String entityType;
    private Long entityId;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;

    public AuditLogResponse(Long id, String actorEmail, String action, String entityType, Long entityId,
                            String oldValue, String newValue, LocalDateTime createdAt) {
        this.id = id;
        this.actorEmail = actorEmail;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getActorEmail() {
        return actorEmail;
    }

    public String getAction() {
        return action;
    }

    public String getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}