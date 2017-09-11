package com.soprasteria.javazone.product;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import com.soprasteria.javazone.SampleData;

public class ProductRepositoryTest {

    private DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:product", "", "");
    private ProductRepository repository = new JdbcProductRepository(dataSource);

    private SampleData sampleData = new SampleData();

    @Test
    public void shouldSaveMinimalProduct() {
        Product product = sampleData.minimalProduct();

        repository.save(product);
        assertThat(repository.retrieve(product.getId()))
            .isEqualToComparingFieldByField(product);
    }

    @Test
    public void shouldListUpdates() {

    }

    @Test
    public void shouldDeleteProduct() {

    }


}
