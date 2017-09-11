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
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        } else {
            int rowCount = executeUpdate("update products set product_name=?, product_category=?, price_in_cents=?, updated_at=? where id=?",
                product.getProductName(), product.getProductCategory(), product.getPriceInCents(), Instant.now(), product.getId());
            if (rowCount > 0) {
                return;
            }
        }
        executeUpdate("insert into products (product_name, product_category, price_in_cents, updated_at, id) values (?, ?, ?, ?, ?)",
            product.getProductName(), product.getProductCategory(), product.getPriceInCents(), Instant.now(), product.getId());
    }

    @Override
    public Product retrieve(UUID id) {
        return queryForObject("select * from products where id = ?", this::mapRow, id);
    }

    @Override
    public List<Product> listChanges(Instant since) {
        return queryForList("select * from products where updated_at > ?", this::mapRow, since);
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(UUID.fromString(rs.getString("id")));
        product.setProductName(rs.getString("product_name"));
        product.setProductCategory(rs.getString("product_category"));
        int priceInCents = rs.getInt("price_in_cents");
        product.setPriceInCents(rs.wasNull() ? null : priceInCents);
        return product;
    }

    @Override
    public void delete(UUID id) {
        executeUpdate("delete from products where id = ?", id);
    }

}
