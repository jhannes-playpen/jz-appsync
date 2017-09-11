package com.soprasteria.javazone.infrastructure.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.soprasteria.javazone.infrastructure.ExceptionHelper;

public class AbstractJdbRepository {

    protected DataSource dataSource;

    public AbstractJdbRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected <T> T queryForObject(String sql, ResultSetMapper<T> mapper, Object... parameters) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setParameters(stmt, parameters);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? mapper.map(rs) : null;
                }
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

    protected <T> List<T> queryForList(String sql, ResultSetMapper<T> mapper, Object... parameters) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setParameters(stmt, parameters);

                try (ResultSet rs = stmt.executeQuery()) {
                    List<T> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapper.map(rs));
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

    protected int executeUpdate(String sql, Object... parameters) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                setParameters(stmt, parameters);
                return stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw ExceptionHelper.soften(e);
        }
    }

    private void setParameters(PreparedStatement stmt, Object... parameters) throws SQLException {
        int index = 1;
        for (Object parameter : parameters) {
            if (parameter instanceof Instant) {
                stmt.setObject(index++, Timestamp.from((Instant)parameter));
            } else {
                stmt.setObject(index++, parameter);
            }
        }
    }

    protected LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }

}