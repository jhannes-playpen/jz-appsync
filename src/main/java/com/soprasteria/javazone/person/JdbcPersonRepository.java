package com.soprasteria.javazone.person;

import java.util.HashMap;
import java.util.Map;

public class JdbcPersonRepository implements PersonRepository {

    private Map<Long, Person> data = new HashMap<>();
    private Person person;

    @Override
    public void save(Person person) {
        this.person = person;
        person.setId((long)data.size());
        data.put(person.getId(), person);
    }

    @Override
    public Person retrieve(long id) {
        return data.get(id);
    }

}
