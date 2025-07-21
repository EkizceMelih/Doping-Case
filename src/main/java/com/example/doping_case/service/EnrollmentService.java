package com.example.doping_case.service;

import com.example.doping_case.dto.AnswerDTO;
import com.example.doping_case.dto.EnrollmentInfoDTO;
import com.example.doping_case.dto.QuestionDTO;
import com.example.doping_case.dto.QuizDTO;
import com.example.doping_case.entity.*;
import com.example.doping_case.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TestService testService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, StudentAnswerRepository studentAnswerRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, TestService testService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.testService = testService;
    }

    public Enrollment createEnrollment(Long studentId, Long testId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        Test test = testService.getById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + testId));

        Enrollment enrollment = new Enrollment(student, test);
        return enrollmentRepository.save(enrollment);
    }

    public StudentAnswer submitAnswer(Long enrollmentId, Long questionId, Long chosenAnswerId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        if (!enrollment.isActive()) {
            throw new IllegalStateException("This test has been finalized and cannot be answered.");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        Answer chosenAnswer = answerRepository.findById(chosenAnswerId)
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + chosenAnswerId));

        if (!question.getTest().getId().equals(enrollment.getTest().getId())) {
            throw new IllegalArgumentException("Question does not belong to the enrolled test.");
        }
        if (!chosenAnswer.getQuestion().getId().equals(question.getId())) {
            throw new IllegalArgumentException("Chosen answer does not belong to the question.");
        }

        StudentAnswer studentAnswer = new StudentAnswer(enrollment, question, chosenAnswer);
        return studentAnswerRepository.save(studentAnswer);
    }

    public Enrollment finalizeTest(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByIdWithQuestions(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        double correctAnswers = 0;
        for (StudentAnswer sa : enrollment.getStudentAnswers()) {
            if (sa.getChosenAnswer().isCorrect()) {
                correctAnswers++;
            }
        }

        double totalQuestions = enrollment.getTest().getQuestions().size();
        double score = (totalQuestions > 0) ? (correctAnswers / totalQuestions) * 100 : 0;

        enrollment.setFinalScore(score);
        enrollment.setActive(false);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Test> findAvailableTestsForStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        Set<Long> enrolledTestIds = enrollments.stream()
                .filter(enrollment -> enrollment.getTest() != null)
                .map(enrollment -> enrollment.getTest().getId())
                .collect(Collectors.toSet());
        
        return testService.getAllTests().stream()
                .filter(test -> !enrolledTestIds.contains(test.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuizDTO getQuizForEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByIdWithQuestions(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        Set<Long> questionIds = enrollment.getTest().getQuestions().stream()
                .map(Question::getId)
                .collect(Collectors.toSet());

        List<Question> questionsWithAnswers = questionRepository.findWithAnswersByIdIn(questionIds);
        Map<Long, Question> questionsMap = questionsWithAnswers.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        List<QuestionDTO> questionDTOs = enrollment.getTest().getQuestions().stream()
                .map(question -> {
                    Question questionWithAnswers = questionsMap.get(question.getId());
                    List<AnswerDTO> answerDTOs = questionWithAnswers.getAnswers().stream()
                            .map(answer -> new AnswerDTO(answer.getId(), answer.getAnswerText()))
                            .collect(Collectors.toList());
                    return new QuestionDTO(question.getId(), question.getQuestionText(), answerDTOs);
                })
                .collect(Collectors.toList());

        return new QuizDTO(enrollment.getId(), enrollment.getTest().getTestName(), enrollment.getStudent().getId(), questionDTOs);
    }
    
    @Transactional(readOnly = true)
    public List<EnrollmentInfoDTO> getByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .filter(enrollment -> enrollment.getFinalScore() != null && !enrollment.isActive())
                .map(enrollment -> new EnrollmentInfoDTO(
                        enrollment.getTest().getTestName(),
                        enrollment.getFinalScore()
                ))
                .collect(Collectors.toList());
    }

    public void deleteEnrollmentsByStudentId(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        // Yazım hatası burada düzeltildi: 'inrollments' -> 'enrollments'
        enrollmentRepository.deleteAll(enrollments);
    }
}