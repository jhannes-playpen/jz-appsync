package com.soprasteria.javazone.person;

import java.time.LocalDate;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;

public class PersonSync {

    private PersonRepository clientRepo;
    private PersonSyncServer server;

    public PersonSync(PersonSyncServer server, PersonRepository clientRepo) {
        this.server = server;
        this.clientRepo = clientRepo;
    }

    public void doSync() {
        JsonArray personJson = server.list();
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
