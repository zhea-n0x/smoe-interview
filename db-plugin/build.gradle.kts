// import com.plugin.db_plugin.model.DatabaseType

plugins {
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.plugin"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("com.mysql:mysql-connector-j:9.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    // website.set("https://github.com/your-org/db-plugin")
    // vcsUrl.set("https://github.com/your-org/db-plugin.git")

    plugins {
        create("dbPlugin") {
            id = "com.plugin.database"

            implementationClass =
                "com.plugin.db_plugin.DatabasePlugin"

            displayName = "Database Gradle Plugin"

            description =
                "Gradle plugin for managing MySQL databases."
        }
    }
}