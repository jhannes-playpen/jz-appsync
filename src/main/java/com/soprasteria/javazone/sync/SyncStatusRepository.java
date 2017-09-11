package com.soprasteria.javazone.sync;

import java.time.Instant;

public interface SyncStatusRepository {

    Instant getLastSyncTime(String tableName);

    void setLastSyncTime(String string, Instant lastSyncTime);

}
