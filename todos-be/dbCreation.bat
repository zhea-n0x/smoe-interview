@echo off
setlocal enabledelayedexpansion

rem Set the script directory and database connection settings
cd /d "%~dp0"
set "DB_NAME=todo_db"
set "DB_USER=postgres"
set "DB_PASSWORD=postgres"

rem Set PostgreSQL password for psql authentication
set "PGPASSWORD=%DB_PASSWORD%"

rem Check if the database exists
set "DB_EXISTS="
for /f "usebackq delims=" %%A in (`psql -h localhost -U "%DB_USER%" -d postgres -tc "SELECT 1 FROM pg_database WHERE datname = '%DB_NAME%'"`) do set "DB_EXISTS=%%A"

if "%DB_EXISTS%"=="1" (
    echo logger: db "%DB_NAME%" already exists.
) else (
    echo logger: create db "%DB_NAME%"...
    psql -h localhost -U "%DB_USER%" -d postgres -c "CREATE DATABASE \"%DB_NAME%\";"
)

echo logger: run flyway migration
call gradlew.bat flywayMigrate

echo logger: run data seeding
call gradlew.bat flywaySeed
