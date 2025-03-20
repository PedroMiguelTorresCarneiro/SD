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
    echo "Compilation completed successfully!"
    
    # Prompt user for parameters
    echo ""
    read -p "Enter the number of voters: " num_voters
    read -p "Enter the internal queue capacity of the voting station: " queue_capacity
    
    # Validate that the entered values are numbers
    if ! [[ "$num_voters" =~ ^[0-9]+$ ]] || ! [[ "$queue_capacity" =~ ^[0-9]+$ ]]; then
        echo "Error: Please enter numeric values only for the parameters."
        exit 1
    fi
    
    # Run the main program with the entered parameters
    echo ""
    echo "Running Main with $num_voters voters and queue capacity $queue_capacity..."
    java -cp "$BUILD_DIR" Main.Main "$num_voters" "$queue_capacity"
else
    echo "Error during compilation!"
    exit 1
fi
