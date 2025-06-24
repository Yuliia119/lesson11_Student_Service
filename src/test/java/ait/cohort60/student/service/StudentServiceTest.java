package ait.cohort60.student.service;

import ait.cohort60.configuration.ServiceConfiguration;
import ait.cohort60.student.dao.StudentRepository;
import ait.cohort60.student.dto.ScoreDto;
import ait.cohort60.student.dto.StudentCredentialsDto;
import ait.cohort60.student.dto.StudentDto;
import ait.cohort60.student.dto.StudentUpdateDto;
import ait.cohort60.student.dto.exceptions.NotFoundExeption;
import ait.cohort60.student.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

///  AAA - Arrange, Act, Assert (подготовка, действие, заявить)

@ContextConfiguration(classes = {ServiceConfiguration.class})
@SpringBootTest
public class StudentServiceTest {
    private final long studentId = 1000L;
    private final String name = "John";
    private final String password = "1234";
    private Student student;

    @Autowired
    private ModelMapper modelMapper;

    @MockitoBean
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    public void setUp(){
        student = new Student(studentId,name,password);
        studentService = new StudentServiceImpl(studentRepository,modelMapper);
    }

    @Test
    void testAddStudentWhenStudentDoesNotExist(){
        // Arrange
        StudentCredentialsDto dto = new StudentCredentialsDto(studentId,name,password);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        // Act
        boolean result = studentService.addStudent(dto);
        // Assert
        assertTrue(result);
    }

    @Test
    void testAddStudentWhenStudentExists(){
        // Arrange
        StudentCredentialsDto dto = new StudentCredentialsDto(studentId,name,password);
        when(studentRepository.existsById(dto.getId())).thenReturn(true);
        // Act
        boolean result = studentService.addStudent(dto);
        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testFindStudentWhenStudentExists(){
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        // Act
        StudentDto studentDto = studentService.findStudent(studentId);
        // Assert
        assertNotNull(studentDto);
        assertEquals(studentId,studentDto.getId());
    }

    @Test
    void testFindStudentWhenStudentNotExists(){
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(NotFoundExeption.class, () -> studentService.findStudent(studentId));
    }

    @Test
     void testRemoveStudent(){
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        // Act
        StudentDto studentDto = studentService.removeStudent(studentId);
        // Assert
        assertNotNull(studentDto);
        assertEquals(studentId,studentDto.getId());
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void testUpdateStudent(){
        // Arrange
        String newName = "NewName";
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        StudentUpdateDto dto = new StudentUpdateDto(newName,null);
        // Act
        StudentCredentialsDto studentCredentialsDto = studentService.updateStudent(studentId,dto);
        // Assert
        assertNotNull(studentCredentialsDto);
        assertEquals(studentId,studentCredentialsDto.getId());
        assertEquals(newName,studentCredentialsDto.getName());
        assertEquals(password,studentCredentialsDto.getPassword());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testAddScoreWhenNewExam(){
        ScoreDto scoreDto = new ScoreDto("Math", 95);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.addScore(studentId,scoreDto);
        assertTrue(result);
        assertEquals(95, student.getScores().get("Math"));
        verify(studentRepository).save(student);
    }

    @Test
    void testAddScoreWhenExamAlreadyExists(){
        student.addScore("Math", 90);
        ScoreDto scoreDto = new ScoreDto("Math", 95);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        boolean result = studentService.addScore(studentId,scoreDto);
        assertFalse(result);
        assertEquals(95, student.getScores().get("Math"));
        verify(studentRepository).save(student);
    }

    @Test
    void testFindStudentByName(){
        when(studentRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(student));
       List<StudentDto> result = studentService.findStudentByName(name);
       assertNotNull(result);
       assertEquals(1,result.size());
       assertEquals(name,result.get(0).getName());
    }

    @Test
    void testCountStudentByName(){
        Set<String> names = Set.of("Peter", "Sofiia");
        when(studentRepository.countByNameIn(names)).thenReturn(2L);
        Long count = studentService.countStudentByName(names);
        assertEquals(2L,count);
    }

    @Test
    void testFindStudentByExamNameMinScore(){
        student.addScore("Math", 95);
        when(studentRepository.findByExamScoreGreaterThan("Math", 90)).thenReturn(List.of(student));
        List<StudentDto> result = studentService.findStudentByExamNameMinScore("Math", 90);
        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(studentId,result.get(0).getId());
    }
}
