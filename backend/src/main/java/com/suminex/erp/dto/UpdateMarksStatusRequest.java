package com.suminex.erp.dto;

import com.suminex.erp.entity.MarksEntryStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateMarksStatusRequest {

    @NotNull(message = "Status is required")
    private MarksEntryStatus status;

    public MarksEntryStatus getStatus() {
        return status;
    }

    public void setStatus(MarksEntryStatus status) {
        this.status = status;
    }
}