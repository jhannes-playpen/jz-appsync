package com.soprasteria.javazone.person;

public class JdbcPersonRepository implements PersonRepository {

    private Person person;

    public void save(Person person) {
        this.person = person;
    }

    public Person retrieve(Long id) {
        return person;
    }

}
