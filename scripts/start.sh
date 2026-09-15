#!/bin/bash

echo "Starting indutranslator Application..."
echo "=============================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
    exit 1
fi

# Check if Java 21 is installed
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" != "21" ]; then
    echo "Warning: Java 21 is required. You have Java $JAVA_VERSION."
fi

echo "Building project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "Error: Build failed."
    exit 1
fi

echo ""
echo "Starting application..."
mvn spring-boot:run
