package ait.cohort60.student.controler;

import ait.cohort60.student.dto.ScoreDto;
import ait.cohort60.student.dto.StudentCredentialsDto;
import ait.cohort60.student.dto.StudentDto;
import ait.cohort60.student.dto.StudentUpdateDto;
import ait.cohort60.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public  class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/student")
    public Boolean addStudent(@RequestBody StudentCredentialsDto studentCredentialsDto) {
        return studentService.addStudent(studentCredentialsDto);
    }

    @GetMapping("/student/{id}")
    public StudentDto findStudent(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @DeleteMapping("/student/{id}")
    public StudentDto removeStudent(@PathVariable Long id) {
        return studentService.removeStudent(id);
    }

    @PostMapping("/student/{id}")
    public StudentCredentialsDto updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDto studentUpdateDto) {
        return studentService.updateStudent(id, studentUpdateDto);
    }

    @PostMapping("score/student/{id}")
    public Boolean addScore(@PathVariable Long id, @RequestBody ScoreDto scoreDto) {
        return studentService.addScore(id, scoreDto);
    }

    @GetMapping("/students/name/{name}")
    public List<StudentDto> findStudentByName(@PathVariable String name) {
        return studentService.findStudentByName(name);
    }

    @GetMapping("/quantity/students")
    public Long countStudentByName(@RequestParam Set<String> names) {
        return studentService.countStudentByName(names);
    }

    @GetMapping("/students/exam/{examName}/minscore/{minScore}")
    public List<StudentDto> findStudentByExamNameMinScore(@PathVariable String examName, @PathVariable Integer minScore) {
        return studentService.findStudentByExamNameMinScore(examName, minScore);
    }
}
