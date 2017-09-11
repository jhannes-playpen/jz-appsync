package com.soprasteria.javazone.person;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.parse.JsonParser;

public class PersonSync {

    private PersonRepository clientRepo;
    private URL serverUrl;
    private Instant lastSyncTime = Instant.ofEpochMilli(0);

    public PersonSync(URL serverUrl, PersonRepository clientRepo) {
        this.serverUrl = serverUrl;
        this.clientRepo = clientRepo;
    }

    public void doSync() throws IOException {
        Instant lastSync = getLastSyncTime();
        for (Person person : readUpdates(getUrl(lastSync)).objects(Person::fromJson)) {
            clientRepo.save(person);
        }
        setLastSyncTime(Instant.now());
    }

    private void setLastSyncTime(Instant lastSyncTime) {
        this.lastSyncTime = lastSyncTime;

    }

    private Instant getLastSyncTime() {
        return lastSyncTime;
    }

    private URL getUrl(Instant since) throws MalformedURLException {
        return new URL(serverUrl, "/api/persons?since=" + since);
    }

    private JsonArray readUpdates(URL url) throws IOException {
        try (InputStream request = url.openStream()) {
            return JsonParser.parseToArray(request);
        }
    }
}
