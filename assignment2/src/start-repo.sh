#!/bin/bash

echo "📦 A iniciar SRepository..."

SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$(cd "$SRC_DIR/.." && pwd)/build"

cd "$SRC_DIR"
java -cp "$BUILD_DIR" serverSide.main.SRepository