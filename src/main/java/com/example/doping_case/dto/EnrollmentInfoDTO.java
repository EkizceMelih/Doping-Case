package com.example.doping_case.dto;

public class EnrollmentInfoDTO {

    private String testName;
    private Double finalScore;

    public EnrollmentInfoDTO(String testName, Double finalScore) {
        this.testName = testName;
        this.finalScore = finalScore;
    }

    // Getters and Setters
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }
}