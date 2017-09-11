package com.soprasteria.javazone.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
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
        executeUpdate("insert into products (id, updated_at) values (?, ?)",
            product.getId(), Instant.now());
    }

    @Override
    public Product retrieve(UUID id) {
        return retrieveById("select * from products where id = ?", rs -> mapRow(rs), id);
    }

    @Override
    public List<Product> listChanges(Instant since) {
        return queryForList("select * from products where updated_at > ?", this::mapRow, since);
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(UUID.fromString(rs.getString("id")));
        return product;
    }

    @Override
    public void delete(UUID id) {
        executeUpdate("delete from products where id = ?", id);
    }

}
