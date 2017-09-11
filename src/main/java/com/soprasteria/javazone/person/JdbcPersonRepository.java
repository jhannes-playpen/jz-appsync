package com.soprasteria.javazone.person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import com.soprasteria.javazone.infrastructure.db.AbstractJdbRepository;

public class JdbcPersonRepository extends AbstractJdbRepository implements PersonRepository {

    public JdbcPersonRepository(DataSource dataSource) {
        super(dataSource);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @Override
    public void save(Person person) {
        if (person.getId() != null) {
            int rowCount = executeUpdate("update persons set first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ? where id = ?",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getDateOfBirth(), person.getId());
            if (rowCount > 0) return;
        } else {
            person.setId(UUID.randomUUID());
        }
        executeUpdate("insert into persons (first_name, middle_name, last_name, date_of_birth, id) values (?, ?, ?, ?, ?)",
            person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getDateOfBirth(), person.getId());
    }

    @Override
    public List<Person> list() {
        return queryForList("select * from persons", this::mapRow);
    }

    @Override
    public Person retrieve(UUID id) {
        return retrieveById("select * from persons where id = ?", this::mapRow, id);
    }

    private Person mapRow(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(UUID.fromString(rs.getString("id")));
        person.setFirstName(rs.getString("first_name"));
        person.setMiddleName(rs.getString("middle_name"));
        person.setLastName(rs.getString("last_name"));
        person.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return person;
    }

    @Override
    public void delete(UUID id) {
        executeUpdate("delete from persons where id = ?", id);
    }

}
