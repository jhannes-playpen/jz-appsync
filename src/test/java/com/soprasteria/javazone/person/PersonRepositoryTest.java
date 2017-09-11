package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

public class PersonRepositoryTest {

    private DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:person", "", "");

    private PersonRepository repository = new JdbcPersonRepository(dataSource);

    private Random random = new Random();

    @Test
    public void shouldRetrieveSavedPerson() {
        Person person = samplePerson();
        repository.save(person);
        assertThat(person).hasNoNullFieldsOrProperties();

        assertThat(repository.retrieve(person.getId()))
            .isEqualToComparingFieldByField(person);

        assertThat(repository.retrieve(-123L)).isNull();
    }

    private Person samplePerson() {
        Person person = new Person();
        person.setFirstName(pickOne(new String[] { "Johannes", "John", "James", "Peter", "Paul" }));
        return person;
    }

    private <T> T pickOne(T[] strings) {
        return strings[random.nextInt(strings.length)];
    }

}
