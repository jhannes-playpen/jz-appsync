package com.soprasteria.javazone.person;

import java.io.IOException;

import org.jsonbuddy.JsonArray;
import com.soprasteria.javazone.infrastructure.server.AbstractSyncServer;

public class PersonSyncServer extends AbstractSyncServer {

    private PersonRepository repository;

    public PersonSyncServer(PersonRepository repository) throws IOException {
        this.repository = repository;
    }

    @Override
    protected void addContextPaths() {
        addJsonContextPath("/api/persons", uri -> {
            return JsonArray.map(repository.list(), Person::toJson);
        });
    }

}
