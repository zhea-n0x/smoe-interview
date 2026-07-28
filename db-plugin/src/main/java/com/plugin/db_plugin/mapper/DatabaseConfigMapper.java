package com.plugin.db_plugin.mapper;

import com.plugin.db_plugin.extension.DatabaseExtension;
import com.plugin.db_plugin.model.DatabaseConfig;
import com.plugin.db_plugin.model.DatabaseType;

public final class DatabaseConfigMapper {

    private DatabaseConfigMapper() {
    }

    public static DatabaseConfig from(DatabaseExtension extension) {

        DatabaseConfig config = new DatabaseConfig();

        config.setType(DatabaseType.valueOf(extension.getType().get().toUpperCase()));

        config.setHost(extension.getHost().get());

        config.setPort(extension.getPort().get());

        config.setDatabase(extension.getDatabase().get());

        config.setUsername(extension.getUsername().get());

        config.setPassword(extension.getPassword().get());

        return config;
    }
}