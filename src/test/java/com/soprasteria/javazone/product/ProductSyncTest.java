package com.soprasteria.javazone.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;
import com.soprasteria.javazone.infrastructure.server.AbstractSyncServer;
import com.soprasteria.javazone.sync.JdbcSyncStatusRepository;
import com.soprasteria.javazone.sync.SyncStatusRepository;

public class ProductSyncTest {

    private SampleData sampleData = new SampleData();

    private ProductRepository clientRepo;

    private DataSource serverDs = JdbcConnectionPool.create("jdbc:h2:mem:server", "", "");
    private ProductRepository serverRepo = new JdbcProductRepository(serverDs);
    private AbstractSyncServer server;

    private ProductSync productSync;

    private SyncStatusRepository syncStatusRepository;

    @Before
    public void startServer() throws IOException {
        server = new ProductSyncServer(serverRepo);
        URL serverUrl = server.start();

        DataSource clientDs = JdbcConnectionPool.create("jdbc:h2:mem:client", "", "");

        clientRepo = new JdbcProductRepository(clientDs);
        syncStatusRepository = new JdbcSyncStatusRepository(clientDs);

        productSync = new ProductSync(serverUrl, clientRepo, syncStatusRepository);
    }

    @Test
    public void shouldSyncNewRows() throws IOException {
        Product product = sampleData.sampleProduct();
        serverRepo.save(product);

        productSync.doSync();

        assertThat(clientRepo.retrieve(product.getId())).isEqualTo(product);
    }

    @Test
    public void shouldSyncMinimalProduct() throws IOException {
        Product minimal = sampleData.minimalProduct();
        serverRepo.save(minimal);

        productSync.doSync();

        assertThat(clientRepo.retrieve(minimal.getId())).isEqualTo(minimal);
    }


}
