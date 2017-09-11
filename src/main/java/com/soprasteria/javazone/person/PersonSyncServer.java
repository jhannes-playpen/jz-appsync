package com.soprasteria.javazone.person;

import java.io.IOException;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;

import com.soprasteria.javazone.infrastructure.server.AbstractSyncServer;

public class PersonSyncServer extends AbstractSyncServer {

    private PersonRepository repository;

    public PersonSyncServer(PersonRepository repository) throws IOException {
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

    @Override
    protected void addContextPaths() {
        addJsonContextPath("/api/persons", uri -> {
            return list();
        });
    }

}
