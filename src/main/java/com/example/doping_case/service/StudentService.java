package com.example.doping_case.service;

import com.example.doping_case.dto.StudentInfoDTO;
import com.example.doping_case.entity.Student;
import com.example.doping_case.repository.StudentRepository;
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
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @CacheEvict(value = "students", allEntries = true)
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    
    @Transactional(readOnly = true)
    public Optional<Student> getById(Long id) {
        return studentRepository.findById(id);
    }
    
    @CacheEvict(value = "students", allEntries = true)
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    @Cacheable("students")
    @Transactional(readOnly = true)
    public List<StudentInfoDTO> getAllStudentsAsInfo() {
        return studentRepository.findAll().stream()
                .map(student -> new StudentInfoDTO(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getStudentNumber()))
                .collect(Collectors.toList());
    }
    
    // Kullanılmayan veya frontend'de karşılığı olmayan diğer metotlar (updateStudent, getByStudentNumber vb.)
    // temizlik açısından kaldırılabilir veya bırakılabilir. Şimdilik bırakıyorum.
    @Transactional(readOnly = true)
    public Optional<Student> getByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }
    
    @CacheEvict(value = "students", allEntries = true)
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setStudentNumber(studentDetails.getStudentNumber());
        return studentRepository.save(student);
    }
}