package com.example.doping_case.controller;

import com.example.doping_case.dto.StudentInfoDTO;
import com.example.doping_case.entity.Student;
import com.example.doping_case.entity.Test;
import com.example.doping_case.service.EnrollmentService;
import com.example.doping_case.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Controller", description = "Öğrenci verileri üzerinde CRUD işlemleri ve ilgili test operasyonları.")
public class StudentController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService; 

    @Autowired
    public StudentController(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Yeni bir öğrenci oluşturur", description = "JSON formatında gönderilen öğrenci bilgilerini veritabanına kaydeder.")
    @ApiResponse(responseCode = "200", description = "Öğrenci başarıyla oluşturuldu ve kaydedilen nesne geri döndürüldü.")
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @Operation(summary = "Tüm öğrencileri listeler", description = "Sistemdeki tüm öğrencilerin temel bilgilerini bir liste olarak döndürür.")
    @ApiResponse(responseCode = "200", description = "Öğrenciler başarıyla listelendi.")
    @GetMapping
    public List<StudentInfoDTO> getAllStudents() {
        return studentService.getAllStudentsAsInfo();
    }

    @Operation(summary = "ID'ye göre tek bir öğrenci getirir")
    @ApiResponse(responseCode = "200", description = "Belirtilen ID'ye sahip öğrenci bulundu.")
    @ApiResponse(responseCode = "404", description = "Belirtilen ID'ye sahip öğrenci bulunamadı.")
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Öğrenci numarasına göre tek bir öğrenci getirir")
    @ApiResponse(responseCode = "200", description = "Belirtilen numaraya sahip öğrenci bulundu.")
    @ApiResponse(responseCode = "404", description = "Belirtilen numaraya sahip öğrenci bulunamadı.")
    @GetMapping("/by-number/{number}")
    public ResponseEntity<Student> getByStudentNumber(@PathVariable("number") String number) {
        return studentService.getByStudentNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Mevcut bir öğrencinin bilgilerini günceller")
    @ApiResponse(responseCode = "200", description = "Öğrenci başarıyla güncellendi.")
    @ApiResponse(responseCode = "404", description = "Güncellenecek öğrenci bulunamadı.")
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        try {
            return ResponseEntity.ok(studentService.updateStudent(id, studentDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "ID'ye göre bir öğrenciyi siler")
    @ApiResponse(responseCode = "204", description = "Öğrenci başarıyla silindi (İçerik döndürmez).")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bir öğrencinin katılabileceği mevcut testleri listeler", description = "Öğrencinin daha önce başlamadığı tüm testleri döndürür.")
    @ApiResponse(responseCode = "200", description = "Katılınabilir testler başarıyla listelendi.")
    @GetMapping("/{id}/available-tests")
    public List<Test> getAvailableTests(@PathVariable Long id) {
        return enrollmentService.findAvailableTestsForStudent(id);
    }
}