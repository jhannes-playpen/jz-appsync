package com.soprasteria.javazone.person;

public interface PersonRepository {

    void save(Person person);

    Person retrieve(Long id);

}
