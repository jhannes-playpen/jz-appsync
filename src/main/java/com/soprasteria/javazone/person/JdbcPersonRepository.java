package com.soprasteria.javazone.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import com.soprasteria.javazone.infrastructure.ExceptionHelper;

public class JdbcPersonRepository implements PersonRepository {

    private DataSource dataSource;

    public JdbcPersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @Override
    public void save(Person person) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("insert into persons (first_name) values (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, person.getFirstName());

                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    person.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

    @Override
    public Person retrieve(long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("select * from persons where id = ?")) {
                stmt.setLong(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Person person = new Person();
                        person.setId(rs.getLong("id"));
                        person.setFirstName(rs.getString("first_name"));
                        return person;
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

}
