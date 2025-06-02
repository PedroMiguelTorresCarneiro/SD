@echo off
echo 🚀 A iniciar servidores restantes...

:: Lê argumentos
set VOTES_TO_END=%1
set NUM_VOTERS=%2
set MAX_INSIDE=%3

echo 🗳️ VOTOS PARA TERMINAR: %VOTES_TO_END%
echo 👥 NÚMERO DE VOTANTES: %NUM_VOTERS%
echo 🏛️ CAPACIDADE DA ESTAÇÃO: %MAX_INSIDE%

:: Define o diretório de origem e de build
set "SRC_DIR=%~dp0"
set "BUILD_DIR=%SRC_DIR%..\build"

:: Remove aspas duplas extras nos comandos
start "SRepository" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" serverSide.main.SRepository %VOTES_TO_END% %NUM_VOTERS% %MAX_INSIDE%
timeout /t 2 >nul

start "SIDCheck" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" serverSide.main.SIDCheck
timeout /t 1 >nul

start "SPollStation" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" serverSide.main.SPollStation
timeout /t 1 >nul

start "SEvotingBooth" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" serverSide.main.SEvotingBooth
timeout /t 1 >nul

start "SExitPoll" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" serverSide.main.SExitPoll
timeout /t 1 >nul

start "CVoter" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" clientSide.main.CVoter
timeout /t 1 >nul

start "CPollClerk" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" clientSide.main.CPollClerk
timeout /t 1 >nul

start "CPollster" cmd /k cd /d "%SRC_DIR%" ^&^& java -cp "%BUILD_DIR%" clientSide.main.CPollster
timeout /t 1 >nul

pause