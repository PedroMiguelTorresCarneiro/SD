#!/bin/bash

echo "🔁 A iniciar sistema distribuído (macOS)..."

# Caminho base
SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$SRC_DIR/../build"

# Criar pasta build se não existir
mkdir -p "$BUILD_DIR"

# Compilar todo o projeto para a pasta build
echo "📦 A compilar o projeto..."
find "$SRC_DIR" -name "*.java" > "$SRC_DIR/sources.txt"
javac -d "$BUILD_DIR" @"$SRC_DIR/sources.txt"
rm "$SRC_DIR/sources.txt"

# Servidores
echo "🚀 [1/2] Servidores"
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SRepository\""
sleep 1
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SIDCheck\""
sleep 1
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SPollStation\""
sleep 1
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SEvotingBooth\""
sleep 1
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SExitPoll\""
sleep 1

# Clientes
echo "🚀 [2/2] Clientes"
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollClerk\""
sleep 1
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollster\""
sleep 1
osascript -e "tell app \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CVoter\""

echo "✅ Simulação distribuída iniciada!"
