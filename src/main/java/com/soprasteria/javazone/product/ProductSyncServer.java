package com.soprasteria.javazone.product;

import java.io.IOException;
import java.time.Instant;

import org.jsonbuddy.JsonArray;

import com.soprasteria.javazone.infrastructure.server.AbstractSyncServer;

public class ProductSyncServer extends AbstractSyncServer {

    private ProductRepository serverRepo;

    public ProductSyncServer(ProductRepository serverRepo) throws IOException {
        this.serverRepo = serverRepo;
    }

    @Override
    protected void addContextPaths() {
        addJsonContextPath("/api/products", uri -> {
            String since = parseParameters(uri.getQuery()).get("since");
            return JsonArray.map(serverRepo.listChanges(Instant.parse(since)), Product::toJson);
        });

    }

}
