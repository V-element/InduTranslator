@echo off
echo Starting indutranslator Application...
echo ==============================
echo.

rem Check if Maven is installed
mvn -v >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed. Please install Maven first.
    exit /b 1
)

rem Check if Java 21 is installed
for /f "tokens=5 delims= " %%v in ('java -version 2^>^&1 ^| findstr version') do (
    set JAVA_VERSION=%%v
)
echo Java version: %JAVA_VERSION%

echo.
echo Building project...
mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo Error: Build failed.
    exit /b 1
)

echo.
echo Starting application...
mvn spring-boot:run
