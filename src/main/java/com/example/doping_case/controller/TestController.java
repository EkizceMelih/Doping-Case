package com.example.doping_case.controller;

import com.example.doping_case.dto.TestInfoDTO;
import com.example.doping_case.entity.Answer;
import com.example.doping_case.entity.Question;
import com.example.doping_case.entity.Test;
import com.example.doping_case.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping
    public Test createTest(@RequestBody Test test) {
        return testService.createTest(test);
    }

    // --- KRİTİK DÜZELTME BURADA ---
    // Bu metot artık Entity listesi yerine DTO listesi döndürüyor.
    @GetMapping
    public List<TestInfoDTO> getAllTests() {
        return testService.getAllTestsAsInfo();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long id) {
        return testService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{testId}/questions")
    public Question addQuestionToTest(@PathVariable Long testId, @RequestBody Question question) {
        return testService.addQuestionToTest(testId, question);
    }

    @PostMapping("/questions/{questionId}/answers")
    public Answer addAnswerToQuestion(@PathVariable Long questionId, @RequestBody Answer answer) {
        return testService.addAnswerToQuestion(questionId, answer);
    }
    
    @GetMapping("/{testId}/questions")
    public ResponseEntity<Set<Question>> getQuestionsForTest(@PathVariable Long testId) {
        return testService.getById(testId)
                .map(test -> ResponseEntity.ok(test.getQuestions()))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}