package com.soprasteria.javazone.person;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.jsonbuddy.JsonArray;

import com.soprasteria.javazone.infrastructure.ExceptionHelper;
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

    private Map<String, String> parseParameters(String query) {
        HashMap<String, String> parameters = new HashMap<>();
        for (String parameterString : query.split("&")) {
            int parts = parameterString.indexOf('=');
            parameters.put(urlDecode(parameterString.substring(0, parts)),
                parts > 0 ? urlDecode(parameterString.substring(parts+1)) : "");
        }
        return parameters;
    }

    private String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw ExceptionHelper.soften(e);
        }
    }

}
