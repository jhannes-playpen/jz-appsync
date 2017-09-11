package com.soprasteria.javazone.product;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import com.soprasteria.javazone.person.JdbcPersonRepository;

public class ProductRepositoryTest {

    private DataSource dataSource = JdbcConnectionPool.create("jdbc:h2:mem:product", "", "");
    private ProductRepository repository = new JdbcProductRepository(dataSource);

    @Test
    public void shouldRetrieveSaved() {
        Product product;

    }

    @Test
    public void shouldListUpdates() {

    }

    @Test
    public void shouldDeleteProduct() {

    }


}
