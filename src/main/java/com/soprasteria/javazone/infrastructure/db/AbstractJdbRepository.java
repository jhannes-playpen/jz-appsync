package com.soprasteria.javazone.infrastructure.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.soprasteria.javazone.infrastructure.ExceptionHelper;
import com.soprasteria.javazone.person.Person;

public class AbstractJdbRepository {

    private DataSource dataSource;

    public AbstractJdbRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Long save(String sql, StatementPreparer preparer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparer.prepare(stmt);

                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

    protected Person retrieveById(String sql, ResultSetMapper<Person> mapper, long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? mapper.map(rs) : null;
                }
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

}