package com.soprasteria.javazone.person;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"firstName", "middleName", "lastName", "dateOfBirth" })
public class Person {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String firstName, middleName, lastName;

    @Getter @Setter
    private LocalDate dateOfBirth;

}
