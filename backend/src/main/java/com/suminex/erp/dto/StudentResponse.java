package com.suminex.erp.dto;

import java.time.LocalDate;

public class StudentResponse {

    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String rollNumber;
    private String prn;
    private LocalDate dateOfBirth;

    public StudentResponse(Long id, Long userId, String email, String firstName, String lastName,
                           String rollNumber, String prn, LocalDate dateOfBirth) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rollNumber = rollNumber;
        this.prn = prn;
        this.dateOfBirth = dateOfBirth;
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

    public String getRollNumber() {
        return rollNumber;
    }

    public String getPrn() {
        return prn;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}