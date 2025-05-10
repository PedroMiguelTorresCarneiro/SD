@echo off
echo 🪞 A iniciar a mainGUI...

cd /d %~dp0

javac -cp . serverSide/main/mainGUI.java
java serverSide.main.mainGUI
