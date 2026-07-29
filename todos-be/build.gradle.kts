buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.postgresql:postgresql:42.7.4")
        classpath("org.flywaydb:flyway-database-postgresql:10.22.0")
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "10.22.0"
    // id("com.plugin.database")
}

group = "com.todo-apps"
version = "0.0.1-SNAPSHOT"

val dbHost = System.getenv("DB_HOST") ?: "localhost"
val dbPort = System.getenv("DB_PORT") ?: "5432"
val dbName = System.getenv("DB_NAME") ?: "todo_db"
val dbUser = System.getenv("DB_USERNAME") ?: "postgres"
val dbPassword = System.getenv("DB_PASSWORD") ?: "postgres"

flyway {
    url = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
    user = dbUser
    password = dbPassword
    locations = arrayOf("classpath:db/migration")
    cleanDisabled = false
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywaySeed") {
    description = "Runs migrations AND seeds dummy data"
    this.locations = arrayOf("classpath:db/migration", "classpath:db/seeds")
}

tasks.named("flywaySeed") {
    group = "database"
    dependsOn("processResources")
}

tasks.named("flywayMigrate") {
    group = "database"
	dependsOn("processResources")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.yaml:snakeyaml:2.4")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// database {
//     type.set("MYSQL")
//     host.set("localhost")
//     port.set(3306)
//     database.set("todo_db")
//     username.set("root")
//     password.set("")
// }