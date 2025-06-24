package ait.cohort60.student.dao;

import ait.cohort60.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface StudentRepository extends MongoRepository<Student, Long> {
    Optional<Student> findByNameIgnoreCase(String name);

    long countByNameIn(Set<String> names);

    @Query("{'scores.?0': {'$gt': ?1}}")
    List<Student> findByExamScoreGreaterThan(String examName, Integer minScore);
}
