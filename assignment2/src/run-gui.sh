#!/bin/bash

echo "🎛️  A iniciar o servidor MainGUI..."

# Diretório do script
SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$(cd "$SRC_DIR/.." && pwd)/build"

# Compilar a mainGUI se necessário
if [ ! -d "$BUILD_DIR" ]; then
    echo "📦 A criar diretório de build em $BUILD_DIR"
    mkdir -p "$BUILD_DIR"
fi

echo "📚 A compilar classes Java..."
javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

# Lançar a mainGUI
osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI\""
