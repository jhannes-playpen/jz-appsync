package com.soprasteria.javazone.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import com.soprasteria.javazone.infrastructure.db.AbstractJdbRepository;

public class JdbcProductRepository extends AbstractJdbRepository implements ProductRepository {

    public JdbcProductRepository(DataSource dataSource) {
        super(dataSource);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @Override
    public void save(Product product) {
        product.setId(UUID.randomUUID());
        executeUpdate("insert into products (id) values (?)",
            product.getId());
    }

    @Override
    public Product retrieve(UUID id) {
        return retrieveById("select * from products where id = ?", rs -> mapRow(rs), id);
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(UUID.fromString(rs.getString("id")));
        return product;
    }

}
