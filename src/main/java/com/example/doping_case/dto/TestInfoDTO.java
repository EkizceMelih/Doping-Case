package com.example.doping_case.dto;

// Sadece test listesi için gerekli bilgileri içerir
public class TestInfoDTO {
    private Long id;
    private String testName;

    // Constructors, Getters, Setters
    public TestInfoDTO(Long id, String testName) {
        this.id = id;
        this.testName = testName;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
}