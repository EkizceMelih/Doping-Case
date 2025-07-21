package com.example.doping_case.dto;

public class StudentInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String studentNumber;

    // Constructors, Getters, Setters
    public StudentInfoDTO(Long id, String firstName, String lastName, String studentNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber = studentNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
}