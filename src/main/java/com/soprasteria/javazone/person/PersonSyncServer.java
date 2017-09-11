package com.soprasteria.javazone.person;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;

public class PersonSyncServer {

    private PersonRepository repository;

    public PersonSyncServer(PersonRepository repository) {
        this.repository = repository;
    }

    public JsonArray list() {
        return JsonArray.map(repository.list(), person ->
            new JsonObject()
            .put("id", person.getId())
            .put("first-name", person.getFirstName())
            .put("middle-name", person.getMiddleName())
            .put("last-name", person.getLastName())
            .put("date-of-birth", person.getDateOfBirth().toString())
            );
    }

}
