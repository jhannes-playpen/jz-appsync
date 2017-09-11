package com.soprasteria.javazone.product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.parse.JsonParser;

import com.soprasteria.javazone.sync.SyncStatusRepository;

public class ProductSync {

    private SyncStatusRepository syncStatusRepository;
    private ProductRepository clientRepo;
    private URL serverUrl;

    public ProductSync(URL serverUrl, ProductRepository clientRepo, SyncStatusRepository syncStatusRepository) {
        this.serverUrl = serverUrl;
        this.clientRepo = clientRepo;
        this.syncStatusRepository = syncStatusRepository;
    }

    public void doSync() throws IOException {
        Instant lastSync = syncStatusRepository.getLastSyncTime("products");
        for (Product products : readUpdates(getUrl(lastSync)).objects(Product::fromJson)) {
            clientRepo.save(products);
        }
        syncStatusRepository.setLastSyncTime("products", Instant.now());
    }

    private URL getUrl(Instant since) throws MalformedURLException {
        return new URL(serverUrl, "/api/products?since=" + since.toString());
    }

    private JsonArray readUpdates(URL url) throws IOException {
        try (InputStream request = url.openStream()) {
            return JsonParser.parseToArray(request);
        }
    }

}
