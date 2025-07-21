package com.example.doping_case.repository;

import com.example.doping_case.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // --- YENİ EKLENEN METOT ---
    // Verilen ID'lere sahip soruları, cevaplarıyla birlikte (JOIN FETCH) getirir.
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.answers WHERE q.id IN :ids")
    List<Question> findWithAnswersByIdIn(@Param("ids") Set<Long> ids);
}