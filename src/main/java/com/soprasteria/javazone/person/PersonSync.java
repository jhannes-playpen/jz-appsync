package com.soprasteria.javazone.person;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.parse.JsonParser;

import com.soprasteria.javazone.sync.SyncStatusRepository;

public class PersonSync {

    private PersonRepository clientRepo;
    private URL serverUrl;
    private SyncStatusRepository syncStatusRepository;

    public PersonSync(URL serverUrl, PersonRepository clientRepo, SyncStatusRepository syncStatusRepository) {
        this.serverUrl = serverUrl;
        this.clientRepo = clientRepo;
        this.syncStatusRepository = syncStatusRepository;
    }

    public void doSync() throws IOException {
        Instant lastSync = getLastSyncTime();
        for (Person person : readUpdates(getUrl(lastSync)).objects(Person::fromJson)) {
            clientRepo.save(person);
        }
        setLastSyncTime(Instant.now());
    }

    private void setLastSyncTime(Instant lastSyncTime) {
        syncStatusRepository.setLastSyncTime("persons", lastSyncTime);
    }

    private Instant getLastSyncTime() {
        return syncStatusRepository.getLastSyncTime("persons");
    }

    private URL getUrl(Instant since) throws MalformedURLException {
        return new URL(serverUrl, "/api/persons?since=" + since.toString());
    }

    private JsonArray readUpdates(URL url) throws IOException {
        try (InputStream request = url.openStream()) {
            return JsonParser.parseToArray(request);
        }
    }
}
