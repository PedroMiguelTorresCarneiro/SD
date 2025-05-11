@echo off
echo 🚀 A iniciar servidores restantes...

:: Define o diretório de origem
set SRC_DIR=%~dp0
set BUILD_DIR=%SRC_DIR%..\build

:: Inicia os servidores em janelas separadas
start "SIDCheck" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SRepository"
timeout /t 3 >nul
start "SIDCheck" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SIDCheck"
timeout /t 1 >nul
start "SPollStation" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SPollStation"
timeout /t 1 >nul
start "SEvotingBooth" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SEvotingBooth"
timeout /t 1 >nul
start "SExitPoll" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" serverSide.main.SExitPoll"
timeout /t 1 >nul

:: Inicia os clientes em janelas separadas
start "CVoter" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" clientSide.main.CVoter"
timeout /t 1 >nul
start "CPollClerk" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" clientSide.main.CPollClerk"
timeout /t 1 >nul
start "CPollster" cmd /k "cd /d "%SRC_DIR%" && java -cp "%BUILD_DIR%" clientSide.main.CPollster"
timeout /t 1 >nul

pause