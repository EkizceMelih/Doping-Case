package com.example.doping_case.dto;

public class AnswerDTO {
    private Long id;
    private String answerText;

    // Constructors, Getters, Setters
    public AnswerDTO(Long id, String answerText) {
        this.id = id;
        this.answerText = answerText;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}