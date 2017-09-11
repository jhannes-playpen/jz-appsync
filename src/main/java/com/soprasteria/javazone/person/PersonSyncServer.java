package com.soprasteria.javazone.person;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;

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

    @SuppressWarnings("restriction")
    public URL start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 0);
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(address, 0);

        String path = "/api/persons";
        server.createContext(path, exchange -> {
            JsonArray response;
            try {
                response = list();
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }

            byte[] buffer = response.toJson().getBytes();
            exchange.sendResponseHeaders(200, buffer.length);
            exchange.getResponseHeaders().add("Content-type", "application/json");
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(buffer);
            }
        });

        server.start();
        address = server.getAddress();
        return new URL("http", address.getHostString(), address.getPort(), path);
    }

}
