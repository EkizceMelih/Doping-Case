package com.example.doping_case.dto;

import java.util.List;

public class QuestionDTO {
    private Long id;
    private String questionText;
    private List<AnswerDTO> answers;

    // Constructors, Getters, Setters
    public QuestionDTO(Long id, String questionText, List<AnswerDTO> answers) {
        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public List<AnswerDTO> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDTO> answers) { this.answers = answers; }
}