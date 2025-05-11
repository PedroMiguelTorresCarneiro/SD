#!/bin/bash

echo "🚀 A iniciar servidores restantes..."

SRC_DIR="$(cd "$(dirname "$0")" && pwd)"

BUILD_DIR="$(cd "$SRC_DIR/.." && pwd)/build"

# Create build directory if it doesn't exist
if [ ! -d "$BUILD_DIR" ]; then
    echo "Creating build directory at $BUILD_DIR"
    mkdir -p "$BUILD_DIR"
fi

# Compile Java classes
echo "Compiling Java classes to $BUILD_DIR"
javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")


  
# Detect OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SRepository\""
    sleep 3
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SIDCheck\""
    sleep 1
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SPollStation\""
    sleep 1
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SEvotingBooth\""
    sleep 1
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SExitPoll\""
    sleep 1
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CVoter\""
    sleep 1
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollClerk\""
    sleep 1
    osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollster\""
else
    # Linux
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SRepository; bash" &
    sleep 3
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SIDCheck; bash" &
    sleep 1
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SPollStation; bash" &
    sleep 1
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SEvotingBooth; bash" &
    sleep 1
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SExitPoll; bash" &
    sleep 1
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CVoter; bash" &
    sleep 1
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollClerk; bash" &
    sleep 1
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollster; bash" &
fi
