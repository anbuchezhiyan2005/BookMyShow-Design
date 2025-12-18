@echo off
echo Starting BookMyShow Backend Server...
echo.

REM Check if MongoDB is accessible
echo Checking MongoDB connection...
timeout /t 2 /nobreak >nul

REM Navigate to backend directory
cd "%~dp0"

REM Compile Java files
echo Compiling Java files...
javac -cp "lib/*" -d bin -encoding UTF-8 api/*.java api/servlets/*.java models/*.java repositories/*.java repositories/cached/*.java services/*.java strategy/*.java config/*.java cache/*.java enums/*.java utils/*.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed! Please check for errors above.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.

REM Start the API server
echo Starting API Server on port 8080...
echo.
java -cp "lib/*;bin" api.ApiServer

pause
