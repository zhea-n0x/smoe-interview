# Seatrium — Todo App

Ringkasan singkat tentang cara menjalankan backend (`todos-be`) dan frontend (`todos-apps`) secara lokal.

## Prasyarat / Prerequisites
- Java 21 (atau gunakan Gradle wrapper yang sudah mengatur toolchain)
- Node.js 18+ dan npm
- PostgreSQL (atau gunakan Docker container PostgreSQL)
- Git (opsional)

Catatan: proyek backend menggunakan Gradle & Spring Boot; frontend menggunakan Next.js.

---

## Menjalankan database PostgreSQL (Docker)
Jika Anda belum punya PostgreSQL lokal, jalankan dengan Docker:

```bash
# Menjalankan PostgreSQL (default: user=postgres, password=postgres, db=todo_db)
docker run --name todo-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=todo_db -p 5432:5432 -d postgres:15
```

Ubah variabel sesuai kebutuhan. Setelah selesai, hentikan & hapus container:

```bash
docker stop todo-postgres
docker rm todo-postgres
```

---

## Menjalankan backend (`todos-be`)
Lokasi: `todos-be/`

Environment variables yang bisa disetel (default sesuai `application.yml`):
- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `5432`)
- `DB_NAME` (default: `todo_db`)
- `DB_USERNAME` (default: `postgres`)
- `DB_PASSWORD` (default: `postgres`)

Langkah menjalankan (Linux/macOS):

```bash
cd todos-be
# Jalankan migrasi dan seed (opsional)
./gradlew flywayMigrate        # hanya migrasi
# atau
./gradlew flywaySeed          # migrasi + seed (perhatian: seeds folder digunakan)

# Jalankan aplikasi Spring Boot
./gradlew bootRun

Alternatif: ada skrip pembantu `runSetup.sh` di `todos-be/` yang menjalankan `dbCreation.sh` lalu memanggil Gradle wrapper. Contoh:

```bash
cd todos-be
./runSetup.sh flywaySeed    # jalankan dbCreation.sh lalu gradle flywaySeed
./runSetup.sh bootRun       # jalankan dbCreation.sh lalu gradle bootRun
```
```

Di Windows PowerShell, gunakan `gradlew.bat`:

```powershell
cd todos-be
.\gradlew.bat flywayMigrate
.\gradlew.bat bootRun
```

Aplikasi backend akan tersedia pada `http://localhost:8080` (default). Endpoint REST berada di `/api/todos`.

Jika ingin menjalankan tanpa Docker, pastikan PostgreSQL berjalan dan database/credential sesuai dengan variabel lingkungan.

---

## Menjalankan frontend (`todos-apps`)
Lokasi: `todos-apps/`

Frontend adalah aplikasi Next.js. Sebelum menjalankan, instal dependensi:

```bash
cd todos-apps
npm install
```
# Seatrium — Todo App

This repository contains two projects:

- `todos-be` — A Spring Boot backend using PostgreSQL and Flyway for migrations.
- `todos-apps` — A Next.js frontend that consumes the backend API.

This README explains how to run both projects locally.

## Prerequisites

- Java 21 (Gradle toolchain is configured to use Java 21)
- Node.js 18+ and npm
- PostgreSQL (or Docker to run PostgreSQL)
- Git (optional)

Notes: The backend uses Spring Boot and Gradle; the frontend is built with Next.js.

---

## Running PostgreSQL (Docker)

If you don't have PostgreSQL installed locally, you can run it with Docker:

```bash
# Run PostgreSQL with defaults: user=postgres, password=postgres, db=todo_db
docker run --name todo-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=todo_db \
  -p 5432:5432 -d postgres:15
```

Adjust environment variables as needed. To stop and remove the container:

```bash
docker stop todo-postgres
docker rm todo-postgres
```

---

## Running the backend (`todos-be`)

Path: `todos-be/`

Environment variables (defaults are in `application.yml`):

- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `5432`)
- `DB_NAME` (default: `todo_db`)
- `DB_USERNAME` (default: `postgres`)
- `DB_PASSWORD` (default: `postgres`)

Run the backend (Linux/macOS):

```bash
cd todos-be
# Run migrations (and seeds if needed)
./gradlew flywayMigrate    # migrate only
# or
./gradlew flywaySeed      # migrate + seed (if seeds are present)

# Run the Spring Boot app
./gradlew bootRun
```

On Windows PowerShell, use `gradlew.bat`:

```powershell
cd todos-be
.\gradlew.bat flywayMigrate
.\gradlew.bat bootRun
```

The backend listens on `http://localhost:8080` by default. The REST API is available under `/api/todos`.

Alternatively, there is a helper script `runSetup.sh` inside `todos-be/` that runs `dbCreation.sh` and then invokes the Gradle wrapper. Example usage:

```bash
cd todos-be
./runSetup.sh flywaySeed    # run dbCreation.sh then gradle flywaySeed
./runSetup.sh bootRun       # run dbCreation.sh then gradle bootRun
```

If you run PostgreSQL outside Docker, make sure the database and credentials match the environment variables.

---

## Running the frontend (`todos-apps`)

Path: `todos-apps/`

Install dependencies:

```bash
cd todos-apps
npm install
```

Run in development mode:

```bash
# Backend default: http://localhost:8080 — no additional config needed
npm run dev
```

If the backend runs on a different host/port, set `NEXT_PUBLIC_API_URL` before starting:

Linux/macOS:

```bash
export NEXT_PUBLIC_API_URL=http://localhost:8080
npm run dev
```

Windows PowerShell:

```powershell
$env:NEXT_PUBLIC_API_URL = 'http://localhost:8080'
npm run dev
```

To build for production:

```bash
npm run build
npm run start
```

Frontend runs at `http://localhost:3000` by default.

---

## Quick start (summary)

1. (Optional) Start PostgreSQL via Docker (see Docker section).
2. Run database migrations and seed data:
   - `cd todos-be` → `./gradlew flywaySeed` (or `flywayMigrate`)
3. Start the backend:
   - `./gradlew bootRun`
4. Start the frontend:
   - `cd todos-apps` → `npm install` → `npm run dev`

---

## Helpful commands & troubleshooting

- If the backend cannot connect to the database: verify `DB_HOST`, `DB_PORT`, `DB_USERNAME`, `DB_PASSWORD`, and ensure Postgres is running.
- To reset migrations during development: `./gradlew flywayClean` then `./gradlew flywayMigrate` (warning: `flywayClean` will drop the schema).
- Check the Spring Boot logs for stack traces and error messages.
- Frontend: if the page is blank, open DevTools and check for failed requests to the API — ensure `NEXT_PUBLIC_API_URL` is correct.

---

## Running tests

- Backend (unit/integration):

```bash
cd todos-be
./gradlew test
```

- Frontend: no tests configured; run the linter if desired:

```bash
cd todos-apps
npm run lint
```

---

If you want, I can also add a `.env.example` for both projects or a `docker-compose.yml` to start the stack together. Let me know which you prefer.