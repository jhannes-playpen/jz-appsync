package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PersonRepositoryTest {

    private PersonRepository repository = new JdbcPersonRepository();

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
        return new Person();
    }

}
