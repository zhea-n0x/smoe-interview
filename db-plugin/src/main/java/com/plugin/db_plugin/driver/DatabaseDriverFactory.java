package com.plugin.db_plugin.driver;

import com.plugin.db_plugin.driver.impl.MySqlDriver;
import com.plugin.db_plugin.model.DatabaseType;

public final class DatabaseDriverFactory {

    private DatabaseDriverFactory() {
    }

    public static DatabaseDriver create(DatabaseType type) {

        return switch (type) {

            case MYSQL -> new MySqlDriver();

        //     case POSTGRESQL ->
        //             throw new UnsupportedOperationException(
        //                     "PostgreSQL is not supported yet."
        //             );

        //     case SQLSERVER ->
        //             throw new UnsupportedOperationException(
        //                     "SQL Server is not supported yet."
        //             );

        //     case ORACLE ->
        //             throw new UnsupportedOperationException(
        //                     "Oracle is not supported yet."
        //             );
        };
    }
}