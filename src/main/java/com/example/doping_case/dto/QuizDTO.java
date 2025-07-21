package com.example.doping_case.dto;

import java.util.List;

public class QuizDTO {
    private Long enrollmentId;
    private String testName;
    private Long studentId; // <-- BU SATIRI EKLEYİN
    private List<QuestionDTO> questions;

    // Constructors, Getters, Setters
     public QuizDTO(Long enrollmentId, String testName, Long studentId, List<QuestionDTO> questions) {
        this.enrollmentId = enrollmentId;
        this.testName = testName;
        this.studentId = studentId; // <-- BU SATIRI EKLEYİN
        this.questions = questions;
    }
    // --- YENİ GETTER/SETTER EKLEYİN ---
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public List<QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDTO> questions) { this.questions = questions; }
}