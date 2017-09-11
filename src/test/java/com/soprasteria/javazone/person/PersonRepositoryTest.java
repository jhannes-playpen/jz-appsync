package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PersonRepositoryTest {

    private PersonRepository repository = new JdbcPersonRepository();

    @Test
    public void shouldRetrieveSavedPerson() {
        Person person = samplePerson();
        assertThat(person).hasNoNullFieldsOrProperties();
        repository.save(person);

        assertThat(repository.retrieve(person.getId()))
            .isEqualToComparingFieldByField(person);
    }

    private Person samplePerson() {
        return new Person();
    }

}
