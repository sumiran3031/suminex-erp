package com.suminex.erp.dto;

public class ResultVerificationResponse {

    private boolean valid;
    private String studentName;
    private int semesterNumber;
    private double sgpa;
    private String message;

    public ResultVerificationResponse(boolean valid, String studentName, int semesterNumber, double sgpa, String message) {
        this.valid = valid;
        this.studentName = studentName;
        this.semesterNumber = semesterNumber;
        this.sgpa = sgpa;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public double getSgpa() {
        return sgpa;
    }

    public String getMessage() {
        return message;
    }
}