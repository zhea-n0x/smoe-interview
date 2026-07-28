package com.plugin.db_plugin.driver;

import com.plugin.db_plugin.model.DatabaseConfig;

public interface DatabaseDriver {

    void create(DatabaseConfig config);

    void drop(DatabaseConfig config);

    boolean exists(DatabaseConfig config);

}