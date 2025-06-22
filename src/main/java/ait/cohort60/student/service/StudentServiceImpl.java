package ait.cohort60.student.service;

import ait.cohort60.student.controler.dao.StudentRepository;
import ait.cohort60.student.dto.ScoreDto;
import ait.cohort60.student.dto.StudentCredentialsDto;
import ait.cohort60.student.dto.StudentDto;
import ait.cohort60.student.dto.StudentUpdateDto;
import ait.cohort60.student.dto.exceptions.NotFoundExeption;
import ait.cohort60.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
        if (studentRepository.findById(studentCredentialsDto.getId()).isPresent()) {
            return false;
        }
        Student student = new Student(studentCredentialsDto.getId(), studentCredentialsDto.getName(), studentCredentialsDto.getPassword());
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentDto removeStudent(Long id) {
        return null;
    }

    @Override
    public StudentCredentialsDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        return null;
    }

    @Override
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        return null;
    }

    @Override
    public List<StudentDto> findStudentByName(String name) {
        return List.of();
    }

    @Override
    public Long countStudentByName(Set<String> names) {
        return 0L;
    }

    @Override
    public List<StudentDto> findStudentByExamNameMinScore(String examName, Integer minScore) {
        return List.of();
    }
}
