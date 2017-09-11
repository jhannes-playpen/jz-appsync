package com.soprasteria.javazone.person;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

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
            Map<String, String> parameters = parseParameters(uri.getQuery());
            Instant since = Instant.parse(parameters.get("since"));
            return JsonArray.map(repository.listChanges(since), Person::toJson);
        });
    }

}
