package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;

public class PersonRepositoryTest {
    private SampleData sampleData = new SampleData();

    private DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:person", "", "");

    private PersonRepository repository = new JdbcPersonRepository(dataSource);

    @Test
    public void shouldRetrieveSavedPerson() {
        Person person = sampleData.samplePerson();
        repository.save(person);
        assertThat(person).hasNoNullFieldsOrProperties();

        assertThat(repository.retrieve(person.getId()))
            .isEqualToComparingFieldByField(person);

        assertThat(repository.retrieve(UUID.randomUUID())).isNull();
    }

    @Test
    public void shouldUpdatePerson() {
        Person original = sampleData.samplePerson();
        repository.save(original);

        Person updated = sampleData.samplePerson();
        updated.setId(original.getId());
        repository.save(updated);

        assertThat(repository.retrieve(original.getId()))
            .isEqualToComparingFieldByField(updated);
    }

    @Test
    public void shouldPersistMinimalPerson() {
        Person person = sampleData.minimalPerson();
        assertThat(person.getMiddleName()).isNull();
        assertThat(person.getDateOfBirth()).isNull();
        repository.save(person);

        assertThat(repository.retrieve(person.getId()))
            .isEqualToComparingFieldByField(person);
    }

    @Test
    public void shouldOnlySyncUpdates() {
        Person person = sampleData.samplePerson();
        repository.save(person);

        repository.delete(person.getId());

        assertThat(repository.retrieve(person.getId())).isNull();
    }
}
