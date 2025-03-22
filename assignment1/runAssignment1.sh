#!/bin/bash

# Project directory
PROJECT_DIR=$(pwd)
SRC_DIR="$PROJECT_DIR/src"
BUILD_DIR="$PROJECT_DIR/build"

# Remove existing build directory if it exists, then create a new one
if [ -d "$BUILD_DIR" ]; then
    rm -rf "$BUILD_DIR"
fi
mkdir -p "$BUILD_DIR"

echo "Compiling all Java files..."

# Compile all .java files directly
find "$SRC_DIR" -name "*.java" | xargs javac -d "$BUILD_DIR" -cp "$SRC_DIR"

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running the program..."
    java -cp "$BUILD_DIR" Main.Main 
else
    echo "Error during compilation!"
    exit 1
fi
