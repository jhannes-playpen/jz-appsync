package com.soprasteria.javazone.person;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsonbuddy.JsonArray;
import org.jsonbuddy.parse.JsonParser;

public class PersonSync {

    private PersonRepository clientRepo;
    private URL serverUrl;

    public PersonSync(URL serverUrl, PersonRepository clientRepo) {
        this.serverUrl = serverUrl;
        this.clientRepo = clientRepo;
    }

    public void doSync() throws IOException {
        for (Person person : readUpdates(getUrl()).objects(Person::fromJson)) {
            clientRepo.save(person);
        }
    }

    private URL getUrl() throws MalformedURLException {
        return new URL(serverUrl, "/api/persons");
    }

    private JsonArray readUpdates(URL url) throws IOException {
        try (InputStream request = url.openStream()) {
            return JsonParser.parseToArray(request);
        }
    }
}
