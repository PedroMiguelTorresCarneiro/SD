#!/bin/bash

echo "🚀 A iniciar servidores restantes..."

VOTES_TO_END=$1
NUM_VOTERS=$2
MAX_INSIDE=$3

echo "🗳️ VOTOS PARA TERMINAR: $VOTES_TO_END"
echo "👥 NÚMERO DE VOTANTES: $NUM_VOTERS"
echo "🏛️ CAPACIDADE DA ESTAÇÃO: $MAX_INSIDE"

SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$(cd "$SRC_DIR/.." && pwd)/build"

# Criar diretório de build se não existir
if [ ! -d "$BUILD_DIR" ]; then
    echo "📁 A criar diretório de build em $BUILD_DIR"
    mkdir -p "$BUILD_DIR"
fi

# Compilar classes
echo "Compiling Java classes to $BUILD_DIR"
javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

# macOS ou Linux (detectar automaticamente o OS)
if [[ "$OSTYPE" == "darwin"* ]]; then
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SRepository $VOTES_TO_END $NUM_VOTERS $MAX_INSIDE\""
    sleep 2
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SIDCheck\""
    sleep 1
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SPollStation\""
    sleep 1
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SEvotingBooth\""
    sleep 1
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SExitPoll\""
    sleep 1
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CVoter\""
    sleep 1
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollClerk\""
    sleep 1
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' clientSide.main.CPollster\""
else
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SRepository $VOTES_TO_END $NUM_VOTERS $MAX_INSIDE; bash" &
    sleep 2
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
