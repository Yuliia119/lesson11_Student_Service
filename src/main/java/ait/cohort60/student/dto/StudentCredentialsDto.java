package ait.cohort60.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCredentialsDto {
    private Long id;
    private String name;
    private String password;

}
///  Добавляем студента