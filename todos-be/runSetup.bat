@echo off
setlocal

cd /d "%~dp0"

echo logger: run database setup...
if exist "%~dp0dbCreation.bat" (
    call "%~dp0dbCreation.bat"
) else (
    echo logger: dbCreation.bat not found
    exit /b 1
)

echo logger: run gradle wrapper...
if exist "%~dp0gradlew.bat" (
    call "%~dp0gradlew.bat" %*
) else (
    echo logger: gradle wrapper not found
    exit /b 1
)
