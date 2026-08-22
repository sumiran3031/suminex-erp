package com.suminex.erp.dto;

public class StaffResponse {

    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String employeeCode;
    private String responsibility;

    public StaffResponse(Long id, Long userId, String email, String firstName, String lastName,
                         String employeeCode, String responsibility) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeCode = employeeCode;
        this.responsibility = responsibility;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getResponsibility() {
        return responsibility;
    }
}