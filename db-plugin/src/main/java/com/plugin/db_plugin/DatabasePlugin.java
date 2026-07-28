package com.plugin.db_plugin;

import com.plugin.db_plugin.extension.DatabaseExtension;
import com.plugin.db_plugin.task.DbCreateTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DatabasePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getExtensions().create(
                "database",
                DatabaseExtension.class
        );

        project.getTasks().register(
                "dbCreate",
                DbCreateTask.class
        );
    }
}