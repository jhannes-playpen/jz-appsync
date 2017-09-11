package com.soprasteria.javazone.infrastructure.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementPreparer {

    void prepare(PreparedStatement stmt) throws SQLException;

}
