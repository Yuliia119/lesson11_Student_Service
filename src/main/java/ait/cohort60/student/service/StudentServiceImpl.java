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
import java.util.stream.Collectors;

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
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        studentRepository.deleteByid(id);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentCredentialsDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        if (studentUpdateDto.getName() != null) {
            student.setName(studentUpdateDto.getName());
        }
        if (studentUpdateDto.getPassword() != null) {
            student.setPassword(studentUpdateDto.getPassword());
        }
        studentRepository.save(student);
        return new StudentCredentialsDto(student.getId(), student.getName(), student.getPassword());
    }

    @Override
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        student.getScores().put(scoreDto.getExamName(), scoreDto.getScore());
        studentRepository.save(student);
        return true;
    }

    @Override
    public List<StudentDto> findStudentByName(String name) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getName().equalsIgnoreCase(name))
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
                .toList();
    }

    @Override
    public Long countStudentByName(Set<String> names) {
        return studentRepository.findAll().stream()
                .filter(student -> names.contains(student.getName()))
                .count();
    }

    @Override
    public List<StudentDto> findStudentByExamNameMinScore(String examName, Integer minScore) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getScores().getOrDefault(examName, 0) >= minScore)
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
                .toList();
    }
}
