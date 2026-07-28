package com.plugin.db_plugin.jdbc;

import com.plugin.db_plugin.model.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JdbcExecutor {

    @FunctionalInterface
    public interface SqlFunction<T> {
        T apply(Statement statement) throws Exception;
    }

    public static <T> T execute(DatabaseConfig config, SqlFunction<T> action) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find MySQL driver!", e);
        }

        String url = String.format("jdbc:mysql://%s:%d/", config.getHost(), config.getPort());

        System.out.println("DatabaseConfig: " + config.getPassword());

        try (Connection connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
             Statement statement = connection.createStatement()) {

            return action.apply(statement);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute SQL: " + e.getMessage(), e);
        }
    }
}