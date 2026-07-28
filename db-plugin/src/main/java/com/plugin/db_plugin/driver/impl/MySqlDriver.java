package com.plugin.db_plugin.driver.impl;

import com.plugin.db_plugin.jdbc.JdbcExecutor;

import com.plugin.db_plugin.driver.DatabaseDriver;
import com.plugin.db_plugin.model.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDriver implements DatabaseDriver {

    @Override
    public void create(DatabaseConfig config) {

        JdbcExecutor.execute(config, statement -> {

            statement.execute(
                    "CREATE DATABASE IF NOT EXISTS `" +
                            config.getDatabase() +
                            "`"
            );

            return null;
        });

    }

    @Override
    public void drop(DatabaseConfig config) {

        JdbcExecutor.execute(config, statement -> {

            statement.execute(
                    "DROP DATABASE IF EXISTS `" +
                            config.getDatabase() +
                            "`"
            );

            return null;
        });

    }

    @Override
    public boolean exists(DatabaseConfig config) {

        String sql = String.format(
                "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='%s'",
                config.getDatabase()
        );

        return JdbcExecutor.execute(config, statement -> {

            var result = statement.executeQuery(sql);

            return result.next();

        });

    }
}