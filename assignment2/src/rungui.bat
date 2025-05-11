@echo off
echo 🎛️  A iniciar o servidor MainGUI...

:: Diretório do script
set SRC_DIR=%~dp0
set BUILD_DIR=%SRC_DIR%\..\build

:: Compilar a mainGUI se necessário
if not exist "%BUILD_DIR%" (
    echo 📦 A criar diretório de build em %BUILD_DIR%
    mkdir "%BUILD_DIR%"
)

echo 📚 A compilar classes Java...
dir /s /b "%SRC_DIR%\*.java" > temp_javalist.txt
javac -d "%BUILD_DIR%" @temp_javalist.txt
del temp_javalist.txt

:: Lançar a mainGUI
start cmd /k "cd /d %SRC_DIR% && java -cp %BUILD_DIR% serverSide.main.SmainGUI"
