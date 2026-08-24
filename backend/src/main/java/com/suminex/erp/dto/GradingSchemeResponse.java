package com.suminex.erp.dto;

import java.util.List;

public class GradingSchemeResponse {

    private Long id;
    private String name;
    private boolean active;
    private List<GradeBandResponse> gradeBands;

    public GradingSchemeResponse(Long id, String name, boolean active, List<GradeBandResponse> gradeBands) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.gradeBands = gradeBands;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<GradeBandResponse> getGradeBands() {
        return gradeBands;
    }
}