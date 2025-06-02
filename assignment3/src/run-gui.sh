# #!/bin/bash

# echo "🎛️  A iniciar o servidor MainGUI..."

# # Diretório do script
# SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
# BUILD_DIR="$(cd "$SRC_DIR/.." && pwd)/build"

# # Compilar a mainGUI se necessário
# if [ ! -d "$BUILD_DIR" ]; then
#     echo "📦 A criar diretório de build em $BUILD_DIR"
#     mkdir -p "$BUILD_DIR"
# fi

# echo "📚 A compilar classes Java..."
# javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

# # Verificar o sistema operacional
# if [[ "$OSTYPE" == "darwin"* ]]; then
#     # macOS
#     echo "🍎 Detetado macOS, a utilizar Terminal..."
#     osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI\""
# elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
#     # Linux
#     echo "🐧 Detetado Linux, a utilizar Gnome Terminal..."
#     gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI; exec bash"
# else
#     echo "⚠️ Sistema operacional não suportado: $OSTYPE"
#     echo "Por favor execute manualmente: cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI"
# fi

#!/bin/bash

echo "🎛️  A iniciar o servidor MainGUI..."

SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$(cd "$SRC_DIR/.." && pwd)/build"

# Compilar classes
if [ ! -d "$BUILD_DIR" ]; then
    echo "📦 A criar diretório de build em $BUILD_DIR"
    mkdir -p "$BUILD_DIR"
fi

echo "📚 A compilar classes Java..."
javac -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

# # Lançar o RMI Registry externo se ainda não estiver a correr
# echo "🛰️  A lançar o RMI Registry externo na porta 1900..."
# cd "$BUILD_DIR"
# osascript -e "tell application \"Terminal\" to do script \"cd '$BUILD_DIR' && rmiregistry 1900\""
# sleep 5
# cd "$SRC_DIR"

# Lançar MainGUI
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "🍎 Detetado macOS, a utilizar Terminal..."
    osascript -e "tell application \"Terminal\" to do script \"cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI\""
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "🐧 Detetado Linux, a utilizar Gnome Terminal..."
    gnome-terminal -- bash -c "cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI; exec bash"
else
    echo "⚠️ Sistema operacional não suportado: $OSTYPE"
    echo "Por favor execute manualmente:"
    echo "1. cd '$BUILD_DIR' && rmiregistry 1900"
    echo "2. cd '$SRC_DIR' && java -cp '$BUILD_DIR' serverSide.main.SmainGUI"
fi
