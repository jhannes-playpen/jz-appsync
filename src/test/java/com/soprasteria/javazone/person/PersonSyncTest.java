package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;

public class PersonSyncTest {

    private SampleData sampleData = new SampleData();

    private DataSource clientDs = JdbcConnectionPool.create("jdbc:h2:mem:client", "", "");
    private PersonRepository clientRepo = new JdbcPersonRepository(clientDs);

    private DataSource serverDs = JdbcConnectionPool.create("jdbc:h2:mem:server", "", "");
    private PersonRepository serverRepo = new JdbcPersonRepository(serverDs);
    private PersonSyncServer server = new PersonSyncServer(serverRepo);

    private PersonSync personSync;

    @Before
    public void startServer() throws IOException {
        URL serverUrl = server.start();
        personSync = new PersonSync(server, serverUrl, clientRepo);
    }


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

    @Test
    public void shouldOnlySyncUpdates() {
        Person original = sampleData.samplePerson();
        serverRepo.save(original);
        personSync.doSync();

        clientRepo.delete(original.getId());
        personSync.doSync();

        assertThat(clientRepo.retrieve(original.getId())).isNull();
    }
}
