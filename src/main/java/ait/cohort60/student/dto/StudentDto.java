package ait.cohort60.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private Map<String, Integer> scores;

}

///  Получаем данные студента
///  Это метод GET, т.е. ответ
