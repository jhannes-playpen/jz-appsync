package com.soprasteria.javazone.person;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

public class JdbcPersonRepository extends AbstractJdbRepository implements PersonRepository {

    public JdbcPersonRepository(DataSource dataSource) {
        super(dataSource);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @Override
    public void save(Person person) {
        Long id = save("insert into persons (first_name) values (?)", stmt -> {
            stmt.setString(1, person.getFirstName());
        });
        person.setId(id);
    }

    @Override
    public Person retrieve(long id) {
        return retrieveById("select * from persons where id = ?", this::mapRow, id);
    }

    private Person mapRow(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        return person;
    }

}
