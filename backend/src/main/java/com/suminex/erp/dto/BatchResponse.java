package com.suminex.erp.dto;

public class BatchResponse {

    private Long id;
    private Long divisionId;
    private String divisionName;
    private String batchName;

    public BatchResponse(Long id, Long divisionId, String divisionName, String batchName) {
        this.id = id;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.batchName = batchName;
    }

    public Long getId() {
        return id;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public String getBatchName() {
        return batchName;
    }
}