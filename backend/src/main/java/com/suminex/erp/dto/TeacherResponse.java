package com.suminex.erp.dto;

public class TeacherResponse {

    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String employeeCode;
    private String designation;

    public TeacherResponse(Long id, Long userId, String email, String firstName, String lastName,
                           String employeeCode, String designation) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeCode = employeeCode;
        this.designation = designation;
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

    public String getDesignation() {
        return designation;
    }
}