package com.plugin.db_plugin.extension;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

public abstract class DatabaseExtension {

    private final Property<String> type;
    private final Property<String> host;
    private final Property<Integer> port;
    private final Property<String> database;
    private final Property<String> username;
    private final Property<String> password;

    @Inject
    public DatabaseExtension(ObjectFactory objects) {

        // Ubah jadi String.class
        this.type = objects.property(String.class); 
        this.host = objects.property(String.class);
        this.port = objects.property(Integer.class);
        this.database = objects.property(String.class);
        this.username = objects.property(String.class);
        this.password = objects.property(String.class);

        this.type.convention("MYSQL");
        this.host.convention("localhost");
        this.port.convention(3306);
    }

    public Property<String> getType() {
        return type;
    }

    public Property<String> getHost() {
        return host;
    }

    public Property<Integer> getPort() {
        return port;
    }

    public Property<String> getDatabase() {
        return database;
    }

    public Property<String> getUsername() {
        return username;
    }

    public Property<String> getPassword() {
        return password;
    }
}