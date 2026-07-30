# Seatrium Interview Task — Todo App

This readme containing how to run the `todos-apps` and `todos-be`

## Prerequisites
- Java 21 (as per this apps using Java 21 for development)
- Node.js 18+ and npm
- PostgreSQL 
- Git (opsional)

Notes: backend using Gradle & Spring Boot; frontend using Next.js.

## Backend initialization
Make sure you already have PostgreSQL installed. And to run the `todos-be`, you just need to run the `runSetup` script inside the `todos-be` folder by change your directory first to `todos-be` folder.

1. For Unix/Linux based CLI or you are using Powershell on Windows, run this script `./runSetup.bat bootRun`. And `runSetup.bat bootRun` if you are using Command Prompt as your CLI interface. This script automatically creating `todos_db` and running the migration and seeder itself.

Notes: 
- Make sure before you run the script, there's no service running on port 8080. Or you can change the port by using additional arguments when run the `runSetup` script. Example, `./runSetup.bat bootRun --args='--server.port=9090'`.
- The REST API is `/api/todos`


## Frontend Initalization
For the frontend, the folder is on `todos-apps`. What you need to do is:

1. Change your directory to `todos-apps`, if you're still on root folder of the `smoe-interview`.
2. Run `npm install` to install the package inside package.json. Use `npm install --verbose`, to view detailed progress of package installation.
3. If there's no issue while installing the package, you can run `npm run dev` command.
4. Then, you can open your apps via this URL `http://localhost:3000/` (adjust the 3000 to your port, if you start the project on different port)

Notes: If you use custom port while starting the `todos-be`. Make sure you also change the API port in `todos-apps/lib/api.js`.

---
