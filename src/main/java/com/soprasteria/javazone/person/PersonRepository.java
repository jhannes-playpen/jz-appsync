package com.soprasteria.javazone.person;

import java.util.List;

public interface PersonRepository {

    void save(Person person);

    Person retrieve(long id);

    List<Person> list();

    void delete(long id);

}
