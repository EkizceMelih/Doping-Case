package com.example.doping_case.controller;

import com.example.doping_case.dto.EnrollmentInfoDTO;
import com.example.doping_case.dto.QuizDTO;
import com.example.doping_case.entity.Enrollment;
import com.example.doping_case.entity.StudentAnswer;
import com.example.doping_case.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentInfoDTO> getByStudent(@PathVariable Long studentId) {
        return enrollmentService.getByStudentId(studentId);
    }

    // --- Test çözme ekranı için DTO döndüren doğru metot ---
    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
        try {
            QuizDTO quizDTO = enrollmentService.getQuizForEnrollment(id);
            return ResponseEntity.ok(quizDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Öğrencinin bir teste başlamasını sağlar ---
    @PostMapping
    public Enrollment createEnrollment(@RequestParam Long studentId, @RequestParam Long testId) {
        return enrollmentService.createEnrollment(studentId, testId);
    }
    
    // --- Öğrencinin bir soruya cevap vermesini sağlar ---
    @PostMapping("/{enrollmentId}/answers")
    public StudentAnswer submitAnswer(@PathVariable Long enrollmentId, 
                                      @RequestParam Long questionId, 
                                      @RequestParam Long chosenAnswerId) {
        return enrollmentService.submitAnswer(enrollmentId, questionId, chosenAnswerId);
    }

    // --- Testi bitirir ve skoru hesaplar ---
    @PostMapping("/{enrollmentId}/finalize")
    public Enrollment finalizeTest(@PathVariable Long enrollmentId) {
        return enrollmentService.finalizeTest(enrollmentId);
    }
    
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<Void> deleteEnrollmentsByStudent(@PathVariable Long studentId) {
        enrollmentService.deleteEnrollmentsByStudentId(studentId);
        return ResponseEntity.noContent().build();
    }
    
    /* // Hata verdiği için bu metot kaldırıldı. İhtiyaç duyulursa,
    // EnrollmentInfoDTO listesi döndürecek şekilde yeniden yazmalıyım.
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }
    */
}