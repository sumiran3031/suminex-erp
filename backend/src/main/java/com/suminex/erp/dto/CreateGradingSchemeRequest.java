package com.suminex.erp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateGradingSchemeRequest {

    @NotBlank(message = "Scheme name is required")
    private String name;

    @NotEmpty(message = "At least one grade band is required")
    @Valid
    private List<GradeBandRequest> gradeBands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GradeBandRequest> getGradeBands() {
        return gradeBands;
    }

    public void setGradeBands(List<GradeBandRequest> gradeBands) {
        this.gradeBands = gradeBands;
    }
}