#!/bin/bash
echo "Starting BookMyShow Backend Server..."
echo

# Navigate to backend directory
cd "$(dirname "$0")"

# Compile Java files
echo "Compiling Java files..."
find . -name "*.java" -print | xargs javac -cp "lib/*" -d bin

if [ $? -ne 0 ]; then
    echo
    echo "Compilation failed! Please check for errors above."
    exit 1
fi

echo
echo "Compilation successful!"
echo

# Start the API server
echo "Starting API Server on port 8080..."
echo
java -cp "lib/*:bin" api.ApiServer
