package com.example.doping_case.service;

import com.example.doping_case.dto.TestInfoDTO;
import com.example.doping_case.entity.Answer;
import com.example.doping_case.entity.Question;
import com.example.doping_case.entity.Test;
import com.example.doping_case.repository.AnswerRepository;
import com.example.doping_case.repository.QuestionRepository;
import com.example.doping_case.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestService {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public TestService(TestRepository testRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @CacheEvict(cacheNames = {"tests", "testsAsInfo"}, allEntries = true)
    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    @Cacheable("tests")
    @Transactional(readOnly = true)
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Test> getById(Long id) {
        return testRepository.findById(id);
    }

    public Question addQuestionToTest(Long testId, Question question) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + testId));
        question.setTest(test);
        return questionRepository.save(question);
    }

    public Answer addAnswerToQuestion(Long questionId, Answer answer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        answer.setQuestion(question);
        return answerRepository.save(answer);
    }

    @CacheEvict(cacheNames = {"tests", "testsAsInfo"}, allEntries = true)
    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }

    @Cacheable("testsAsInfo")
    @Transactional(readOnly = true)
    public List<TestInfoDTO> getAllTestsAsInfo() {
        return testRepository.findAll().stream()
                .map(test -> new TestInfoDTO(test.getId(), test.getTestName()))
                .collect(Collectors.toList());
    }
}