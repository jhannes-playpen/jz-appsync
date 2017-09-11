package com.soprasteria.javazone.sync;

import java.time.Instant;
import java.util.Optional;

import javax.sql.DataSource;

import com.soprasteria.javazone.infrastructure.db.AbstractJdbRepository;

public class JdbcSyncStatusRepository extends AbstractJdbRepository implements SyncStatusRepository {

    public JdbcSyncStatusRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Instant getLastSyncTime(String tableName) {
        return Optional.ofNullable(retrieveById("select * from sync_status where table_name = ?", rs -> rs.getTimestamp("last_sync_time"), tableName))
            .map(t -> t.toInstant())
            .orElse(Instant.ofEpochMilli(0));
    }

    @Override
    public void setLastSyncTime(String tableName, Instant lastSyncTime) {
        int rowCount = executeUpdate("update sync_status set last_sync_time = ? where table_name = ?",
            lastSyncTime, tableName);
        if (rowCount == 0) {
            executeUpdate("insert into sync_status (last_sync_time, table_name) values (?, ?)",
                lastSyncTime, tableName);
        }
    }

}
