buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.mysql:mysql-connector-j:8.3.0")
        classpath("org.flywaydb:flyway-mysql:10.22.0")
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "10.22.0"
    id("com.plugin.database")
}

group = "com.todo-apps"
version = "0.0.1-SNAPSHOT"

flyway {
    url = "jdbc:mysql://localhost:3306/todo_db"
    user = "root"
    password = ""
    locations = arrayOf("classpath:db/migration")
	cleanDisabled = false
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("flywaySeed") {
    description = "Runs migrations AND seeds dummy data"
    this.locations = arrayOf("classpath:db/migration", "classpath:db/seeds")
}

tasks.named("flywaySeed") {
    group = "database"
    dependsOn("dbCreate")
    dependsOn("processResources")
}

tasks.named("flywayMigrate") {
    group = "database"
    dependsOn("dbCreate")
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
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.yaml:snakeyaml:2.4")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

database {
    type.set("MYSQL")
    host.set("localhost")
    port.set(3306)
    database.set("todo_db")
    username.set("root")
    password.set("")
}