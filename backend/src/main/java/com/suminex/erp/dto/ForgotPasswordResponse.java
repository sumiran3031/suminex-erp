package com.suminex.erp.dto;

public class ForgotPasswordResponse {

    private String message;
    private String devOnlyOtp;

    public ForgotPasswordResponse(String message, String devOnlyOtp) {
        this.message = message;
        this.devOnlyOtp = devOnlyOtp;
    }

    public String getMessage() {
        return message;
    }

    public String getDevOnlyOtp() {
        return devOnlyOtp;
    }
}