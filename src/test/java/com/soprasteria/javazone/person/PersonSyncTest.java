package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Ignore;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;

public class PersonSyncTest {

    private SampleData sampleData = new SampleData();

    private DataSource clientDs = JdbcConnectionPool.create("jdbc:h2:mem:client", "", "");
    private PersonRepository clientRepo = new JdbcPersonRepository(clientDs);

    private DataSource serverDs = JdbcConnectionPool.create("jdbc:h2:mem:server", "", "");
    private PersonRepository serverRepo = new JdbcPersonRepository(serverDs);

    private PersonSync personSync = new PersonSync(serverRepo, clientRepo);

    @Test
    public void shouldNotSyncAutomatically() {
        Person person = sampleData.samplePerson();
        serverRepo.save(person);

        assertThat(clientRepo.retrieve(person.getId())).isNull();
    }

    @Test
    public void shouldSyncNewRows() {
        Person person = sampleData.samplePerson();
        serverRepo.save(person);

        personSync.doSync();

        assertThat(clientRepo.retrieve(person.getId())).isEqualTo(person);
    }

    @Test
    @Ignore
    public void shouldSyncUpdates() {
        Person original = sampleData.samplePerson();
        serverRepo.save(original);
        personSync.doSync();

        Person updated = sampleData.samplePerson();
        updated.setId(original.getId());
        serverRepo.save(updated);
        personSync.doSync();


        assertThat(clientRepo.retrieve(original.getId()))
            .isEqualToComparingFieldByField(updated);
    }

}
