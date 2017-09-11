package com.soprasteria.javazone.person;

import java.util.List;
import java.util.UUID;

public interface PersonRepository {

    void save(Person person);

    Person retrieve(UUID id);

    List<Person> list();

    void delete(UUID id);

}
