package com.soprasteria.javazone.person;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import com.soprasteria.javazone.infrastructure.ExceptionHelper;
import com.soprasteria.javazone.infrastructure.db.AbstractJdbRepository;
import com.soprasteria.javazone.infrastructure.db.ResultSetMapper;

public class JdbcPersonRepository extends AbstractJdbRepository implements PersonRepository {

    public JdbcPersonRepository(DataSource dataSource) {
        super(dataSource);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @Override
    public void save(Person person) {
        Long id = save("insert into persons (first_name, middle_name, last_name, date_of_birth) values (?, ?, ?, ?)", stmt -> {
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getMiddleName());
            stmt.setString(3, person.getLastName());
            stmt.setDate(4, Date.valueOf(person.getDateOfBirth()));
        });
        person.setId(id);
    }

    @Override
    public List<Person> list() {
        return queryForList("select * from persons", this::mapRow);
    }

    protected List<Person> queryForList(String sql, ResultSetMapper<Person> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                try (ResultSet rs = stmt.executeQuery()) {
                    List<Person> result = new ArrayList<>();
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

    @Override
    public Person retrieve(long id) {
        return retrieveById("select * from persons where id = ?", this::mapRow, id);
    }

    private Person mapRow(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setMiddleName(rs.getString("middle_name"));
        person.setLastName(rs.getString("last_name"));
        person.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return person;
    }

}
