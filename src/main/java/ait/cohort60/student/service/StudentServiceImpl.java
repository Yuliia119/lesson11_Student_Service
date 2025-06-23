package ait.cohort60.student.service;

import ait.cohort60.student.controler.dao.StudentRepository;
import ait.cohort60.student.dto.ScoreDto;
import ait.cohort60.student.dto.StudentCredentialsDto;
import ait.cohort60.student.dto.StudentDto;
import ait.cohort60.student.dto.StudentUpdateDto;
import ait.cohort60.student.dto.exceptions.NotFoundExeption;
import ait.cohort60.student.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override    // добавляем нового студента
    public Boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
        if (studentRepository.findById(studentCredentialsDto.getId()).isPresent()) {
            return false;
        }
        Student student = new Student(studentCredentialsDto.getId(), studentCredentialsDto.getName(), studentCredentialsDto.getPassword());
        studentRepository.save(student); //сохраняем студента
        return true;
    }

    @Override  /// ищем инфу о студенте по ID
    public StudentDto findStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override  /// удаление студента
    public StudentDto removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        studentRepository.deleteById(id);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override  /// обновляет имя и пароль студента
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

    @Override  /// добавляет новый экзамен и бал студента
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundExeption::new);
        Boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
        studentRepository.save(student);
        return res;
    }

    @Override  /// находит студента по имени
    public List<StudentDto> findStudentByName(String name) {
        return studentRepository.findByNameIgnoreCase(name)
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
                .toList();
    }

    @Override /// считает студентов с определёнными именами
    public Long countStudentByName(Set<String> names) {
        return studentRepository.countByNameIn(names);
    }

    @Override  ///  находит студентов с оценками по предмету
    public List<StudentDto> findStudentByExamNameMinScore(String examName, Integer minScore) {
        return studentRepository.findByExamScoreGreaterThan(examName, minScore)
                .stream()
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
                .toList();
    }

}
