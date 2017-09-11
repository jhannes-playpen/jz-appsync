package com.soprasteria.javazone.person;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;
import org.jsonbuddy.parse.JsonParser;

public class PersonSync {

    private PersonRepository clientRepo;
    private PersonSyncServer server;
    private URL serverUrl;

    public PersonSync(PersonSyncServer server, URL serverUrl, PersonRepository clientRepo) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.clientRepo = clientRepo;
    }

    public void doSync() throws IOException {
        JsonArray personJson;
        try (InputStream request = serverUrl.openStream()) {
            personJson = JsonParser.parseToArray(request);
        }
        for (Person person : personJson.objects(this::toPerson)) {
            clientRepo.save(person);
        }
    }

    private Person toPerson(JsonObject json) {
        Person person = new Person();
        person.setId(json.requiredLong("id"));
        person.setFirstName(json.requiredString("first-name"));
        person.setMiddleName(json.requiredString("middle-name"));
        person.setLastName(json.requiredString("last-name"));
        person.setDateOfBirth(LocalDate.parse(json.requiredString("date-of-birth")));
        return person;
    }

}
