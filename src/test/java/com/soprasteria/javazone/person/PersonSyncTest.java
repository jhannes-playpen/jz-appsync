package com.soprasteria.javazone.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;
import com.soprasteria.javazone.infrastructure.server.AbstractSyncServer;
import com.soprasteria.javazone.sync.JdbcSyncStatusRepository;
import com.soprasteria.javazone.sync.SyncStatusRepository;

public class PersonSyncTest {

    private SampleData sampleData = new SampleData();

    private PersonRepository clientRepo;

    private DataSource serverDs = JdbcConnectionPool.create("jdbc:h2:mem:server", "", "");
    private PersonRepository serverRepo = new JdbcPersonRepository(serverDs);
    private AbstractSyncServer server;

    private PersonSync personSync;

    private SyncStatusRepository syncStatusRepository;

    @Before
    public void startServer() throws IOException {
        server = new PersonSyncServer(serverRepo);
        URL serverUrl = server.start();

        DataSource clientDs = JdbcConnectionPool.create("jdbc:h2:mem:client", "", "");
        Flyway flyway = new Flyway();
        flyway.setDataSource(clientDs);
        flyway.clean();
        flyway.migrate();

        clientRepo = new JdbcPersonRepository(clientDs);
        syncStatusRepository = new JdbcSyncStatusRepository(clientDs);

        personSync = new PersonSync(serverUrl, clientRepo, syncStatusRepository);
    }

    @Test
    public void shouldNotSyncAutomatically() {
        Person person = sampleData.samplePerson();
        serverRepo.save(person);

        assertThat(clientRepo.retrieve(person.getId())).isNull();
    }

    @Test
    public void shouldSyncNewRows() throws IOException {
        Person person = sampleData.samplePerson();
        serverRepo.save(person);

        personSync.doSync();

        assertThat(clientRepo.retrieve(person.getId())).isEqualTo(person);
    }

    @Test
    public void shouldSyncUpdates() throws IOException, InterruptedException {
        Person original = sampleData.samplePerson();
        serverRepo.save(original);
        personSync.doSync();
        Thread.sleep(10);

        Person updated = sampleData.samplePerson();
        updated.setId(original.getId());
        serverRepo.save(updated);
        personSync.doSync();

        assertThat(clientRepo.retrieve(original.getId()))
            .isEqualToComparingFieldByField(updated);
    }

    @Test
    public void shouldOnlySyncUpdates() throws IOException {
        Person original = sampleData.samplePerson();
        serverRepo.save(original);
        personSync.doSync();

        clientRepo.delete(original.getId());
        personSync.doSync();

        assertThat(clientRepo.retrieve(original.getId())).isNull();
    }

    @Test
    public void shouldMergeWithLocalUpdates() throws IOException {
        Person clientPerson = sampleData.samplePerson();
        clientRepo.save(clientPerson);

        Person serverPerson = sampleData.samplePerson();
        serverRepo.save(serverPerson);
        personSync.doSync();

        assertThat(clientRepo.retrieve(clientPerson.getId()))
            .isEqualToComparingFieldByField(clientPerson);
        assertThat(clientRepo.retrieve(serverPerson.getId()))
            .isEqualToComparingFieldByField(serverPerson);
    }
}
