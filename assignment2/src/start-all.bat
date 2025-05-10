@echo off
echo 🔁 A iniciar sistema distribuído (Windows)...

:: Caminho base
set SRC_DIR=%~dp0
set BUILD_DIR=%SRC_DIR%..\build

:: Criar pasta build se não existir
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"

:: Compilar todo o projeto para a pasta build
echo 📦 A compilar o projeto...
dir /s /b "%SRC_DIR%\*.java" > "%SRC_DIR%\sources.txt"
javac -d "%BUILD_DIR%" -encoding UTF-8 @"%SRC_DIR%\sources.txt"
del "%SRC_DIR%\sources.txt"

:: Servidores
echo 🚀 [1/2] Servidores
start "SRepository" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SRepository"
timeout /t 1 >nul
start "SIDCheck" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SIDCheck"
timeout /t 1 >nul
start "SPollStation" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SPollStation"
timeout /t 1 >nul
start "SEvotingBooth" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SEvotingBooth"
timeout /t 1 >nul
start "SExitPoll" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SExitPoll"
timeout /t 1 >nul

:: Clientes«'
start "CVoter" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" clientSide.main.CVoter"
timeout /t 1 >nul
start "CPollClerk" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" clientSide.main.CPollClerk"
timeout /t 1 >nul
start "CPollster" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" clientSide.main.CPollster"
timeout /t 1 >nul

echo ✅ Simulação distribuída iniciada!
pause