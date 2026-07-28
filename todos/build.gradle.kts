plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.todo-apps"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")
	implementation("org.yaml:snakeyaml:2.4")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	
}

tasks.withType<Test> {
	useJUnitPlatform()
}

import java.sql.DriverManager

tasks.register("createDatabase") {
    doLast {
        val yaml = org.yaml.snakeyaml.Yaml()
		val config = yaml.load<Map<String, Any>>(
			file("src/main/resources/application.yml").inputStream()
		)

		val spring = config["spring"] as Map<*, *>
		val datasource = spring["datasource"] as Map<*, *>

		val jdbcUrl = datasource["url"] as String
		val username = datasource["username"] as String
		val password = datasource["password"] as String

        DriverManager.getConnection(url, username, password).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("CREATE DATABASE IF NOT EXISTS todo_db")
            }
        }

        println("db created successfully (or already exists)")
    }
}