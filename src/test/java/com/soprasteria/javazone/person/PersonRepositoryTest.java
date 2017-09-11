package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
        String[] lastNames = new String[] { "Brodwall", "Adams", "Smith", "Jones" };

        Person person = new Person();
        person.setFirstName(pickOne(new String[] { "Johannes", "John", "James", "Peter", "Paul" }));
        person.setMiddleName(pickOne(lastNames));
        person.setLastName(pickOne(lastNames));
        person.setDateOfBirth(randomBirthDate());
        return person;
    }

    private LocalDate randomBirthDate() {
        return LocalDate.now().minusYears(20).minusDays(random.nextInt(365*30));
    }

    private <T> T pickOne(T[] strings) {
        return strings[random.nextInt(strings.length)];
    }

}
