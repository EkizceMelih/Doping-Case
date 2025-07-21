package com.example.doping_case.repository;

import com.example.doping_case.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByTestId(Long testId);

    // --- BU METOT GÜNCELLENDİ ---
    // Artık sadece soruları çekiyor, cevapları değil.
    @Query("SELECT e FROM Enrollment e " +
           "LEFT JOIN FETCH e.test t " +
           "LEFT JOIN FETCH t.questions " +
           "WHERE e.id = :id")
    Optional<Enrollment> findByIdWithQuestions(@Param("id") Long id);
}