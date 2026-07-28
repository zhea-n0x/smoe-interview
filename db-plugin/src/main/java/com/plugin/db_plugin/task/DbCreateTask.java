package com.plugin.db_plugin.task;

import com.plugin.db_plugin.driver.DatabaseDriver;
import com.plugin.db_plugin.driver.impl.MySqlDriver;
import com.plugin.db_plugin.extension.DatabaseExtension;
import com.plugin.db_plugin.mapper.DatabaseConfigMapper;
import com.plugin.db_plugin.model.DatabaseConfig;
import com.plugin.db_plugin.model.DatabaseType;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public abstract class DbCreateTask extends DefaultTask {

    @TaskAction
    public void executeTask() {

        DatabaseExtension extension =
                getProject()
                        .getExtensions()
                        .getByType(DatabaseExtension.class);

        DatabaseConfig config =
                DatabaseConfigMapper.from(extension);

        // Pilih driver berdasarkan tipe database
        DatabaseDriver driver;
        if (config.getType() == DatabaseType.MYSQL) {
            driver = new MySqlDriver();
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + config.getType());
        }

        driver.create(config);

        getLogger().lifecycle(
                "Database '{}' created successfully.",
                config.getDatabase()
        );

    }

}