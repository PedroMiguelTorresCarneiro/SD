#!/bin/bash

echo "🚀 A iniciar servidores restantes..."

SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$SRC_DIR/../build"

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
sleep 1

