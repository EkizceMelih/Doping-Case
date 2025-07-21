package com.example.doping_case.service;

import com.example.doping_case.dto.StudentInfoDTO;
import com.example.doping_case.entity.Student;
import com.example.doping_case.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Bu annotasyon, testimizde Mockito kütüphanesini aktif hale getirir.
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    // @Mock: Sahte bir StudentRepository oluşturur. Bu, gerçek veritabanına gitmez.
    @Mock
    private StudentRepository studentRepository;

    // @InjectMocks: Test edeceğimiz asıl StudentService nesnesini oluşturur.
    // Mockito, yukarıda oluşturulan sahte studentRepository'yi bu nesnenin içine otomatik olarak enjekte eder.
    @InjectMocks
    private StudentService studentService;

    private Student student;

    // @BeforeEach: Her bir test metodu çalışmadan önce bu metot çalışır.
    // Testler için ortak bir başlangıç verisi oluşturmak için kullanılır.
    @BeforeEach
    void setUp() {
        student = new Student("Test", "Öğrenci", "999");
        student.setId(1L);
    }

    @Test
    void createStudent_shouldReturnSavedStudent() {
        // ARRANGE (Hazırlık): Testin koşullarını hazırlarız.
        // Sahte repository'nin save() metodu çağrıldığında ne yapacağını söylüyoruz:
        // "Sana verilen herhangi bir Student nesnesini kaydet ve aynısını geri döndür."
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // ACT (Eylem): Test edeceğimiz asıl metodu çağırırız.
        Student createdStudent = studentService.createStudent(new Student("Test", "Öğrenci", "999"));

        // ASSERT (Doğrulama): Sonucun beklediğimiz gibi olup olmadığını kontrol ederiz.
        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getStudentNumber()).isEqualTo("999");
    }

    @Test
    void getAllStudentsAsInfo_shouldReturnStudentInfoDTOList() {
        // ARRANGE (Hazırlık):
        // Sahte repository'nin findAll() metodu çağrıldığında, bizim oluşturduğumuz
        // tek öğrencilik bir liste döndürmesini söylüyoruz.
        when(studentRepository.findAll()).thenReturn(List.of(student));

        // ACT (Eylem):
        List<StudentInfoDTO> students = studentService.getAllStudentsAsInfo();

        // ASSERT (Doğrulama):
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("Test");
    }

    @Test
    void getById_whenStudentExists_shouldReturnStudent() {
        // ARRANGE (Hazırlık):
        // Sahte repository'nin findById(1L) metodu çağrıldığında, bizim oluşturduğumuz
        // öğrenciyi içeren bir Optional döndürmesini söylüyoruz.
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // ACT (Eylem):
        Optional<Student> foundStudent = studentService.getById(1L);

        // ASSERT (Doğrulama):
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getId()).isEqualTo(1L);
    }
}