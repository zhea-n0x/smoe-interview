package com.plugin.db_plugin.jdbc;

import com.plugin.db_plugin.model.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JdbcConnectionFactory {

    private JdbcConnectionFactory() {
    }

    public static Connection create(DatabaseConfig config) throws SQLException {

        String url = String.format(
                "jdbc:mysql://%s:%d/",
                config.getHost(),
                config.getPort()
        );

        return DriverManager.getConnection(
                url,
                config.getUsername(),
                config.getPassword()
        );
    }
}