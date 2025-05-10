#!/bin/bash

echo "🪞 A iniciar a mainGUI..."

cd "$(dirname "$0")" || exit 1

javac -cp . serverSide/main/mainGUI.java
java serverSide.main.mainGUI
