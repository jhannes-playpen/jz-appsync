package com.soprasteria.javazone.person;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
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
        Instant now = Instant.now();
        if (person.getId() != null) {
            int rowCount = executeUpdate("update persons set first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ?, updated_at = ? where id = ?",
                person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getDateOfBirth(), now, person.getId());
            if (rowCount > 0) return;
        } else {
            person.setId(UUID.randomUUID());
        }
        executeUpdate("insert into persons (first_name, middle_name, last_name, date_of_birth, updated_at, id) values (?, ?, ?, ?, ?, ?)",
            person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getDateOfBirth(), now, person.getId());
    }

    @Override
    public List<Person> list() {
        return queryForList("select * from persons", this::mapRow);
    }

    @Override
    public List<Person> listChanges(Instant since) {
        return queryForList("select * from persons where updated_at > ?", this::mapRow, since);
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
        person.setDateOfBirth(toLocalDate(rs.getDate("date_of_birth")));
        return person;
    }

    private LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    @Override
    public void delete(UUID id) {
        executeUpdate("delete from persons where id = ?", id);
    }

}
