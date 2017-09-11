package com.soprasteria.javazone.infrastructure.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import org.jsonbuddy.JsonArray;

@SuppressWarnings("restriction")
public abstract class AbstractSyncServer {

    private com.sun.net.httpserver.HttpServer server;

    public AbstractSyncServer() throws IOException {
        InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 0);
        server = com.sun.net.httpserver.HttpServer.create(address, 0);
    }

    public URL start() throws IOException {
        addContextPaths();
        server.start();
        InetSocketAddress address = server.getAddress();
        return new URL("http", address.getHostString(), address.getPort(), "");
    }

    protected abstract void addContextPaths();

    protected void addJsonContextPath(String path, Function<URI, JsonArray> f) {
        server.createContext(path, exchange -> {
            JsonArray response;
            try {
                response = f.apply(exchange.getRequestURI());
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
    }

}
