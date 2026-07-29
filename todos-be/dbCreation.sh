#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DB_NAME="todo_db"
DB_USER="postgres"
DB_PASSWORD="postgres"

export PGPASSWORD="$DB_PASSWORD"

if ! psql -h localhost -U "$DB_USER" -d postgres -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1; then
  echo "logger: create db '$DB_NAME'..."
  psql -h localhost -U "$DB_USER" -d postgres -c "CREATE DATABASE \"$DB_NAME\";"
fi

echo "logger: run flyway migration"
cd "$SCRIPT_DIR"
./gradlew flywayMigrate

echo "logger: run data seeding"
./gradlew flywaySeed