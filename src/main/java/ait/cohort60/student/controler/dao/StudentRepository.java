package ait.cohort60.student.controler.dao;

import ait.cohort60.student.model.Student;


import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(Long id);
    void deleteByid(Long id);
    List<Student> findAll();

}
