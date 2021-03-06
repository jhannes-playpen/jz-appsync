package com.soprasteria.javazone.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;

public class ProductRepositoryTest {

    private DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:product", "", "");
    private ProductRepository repository = new JdbcProductRepository(dataSource);

    private SampleData sampleData = new SampleData();

    @Test
    public void shouldRetrieveSavedProduct() {
        Product product = sampleData.sampleProduct();
        UUID id = product.getId();
        assertThat(product).hasNoNullFieldsOrProperties();

        repository.save(product);
        assertThat(repository.retrieve(id))
            .isEqualToComparingFieldByField(product);
    }

    @Test
    public void shouldSaveMinimalProduct() {
        Product product = sampleData.minimalProduct();

        repository.save(product);
        assertThat(repository.retrieve(product.getId()))
            .isEqualToComparingFieldByField(product);
    }

    @Test
    public void shouldListUpdates() throws InterruptedException {
        Product oldProduct = sampleData.sampleProduct();
        repository.save(oldProduct);
        Thread.sleep(10);
        Instant since = Instant.now();
        Thread.sleep(10);
        Product newProduct = sampleData.sampleProduct();
        repository.save(newProduct);

        assertThat(repository.listChanges(since))
            .contains(newProduct)
            .doesNotContain(oldProduct);
    }

    @Test
    public void shouldDeleteProduct() {
        Product product = sampleData.sampleProduct();
        repository.save(product);

        repository.delete(product.getId());

        assertThat(repository.retrieve(product.getId())).isNull();
    }

    @Test
    public void shouldUpdateProduct() {
        Product original = sampleData.sampleProduct();
        repository.save(original);

        Product updated = sampleData.sampleProduct();
        updated.setId(original.getId());
        repository.save(updated);

        assertThat(repository.retrieve(original.getId()))
            .isEqualToComparingFieldByField(updated);
    }
}
