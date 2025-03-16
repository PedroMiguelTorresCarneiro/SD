# SD - Distributed Systems


## Threads

### Voters

### Poll Clerk

### Pollster

````
📂 src/
┣ 📂 Monitors/ (Contém os Monitors & Interfaces)
┃ ┣ 📂 PollStation/
┃ ┃ ┣ 📜 IPollStation.java
┃ ┃ ┣ 📜 MPollStation.java
┃ ┣ 📂 IDCheck/
┃ ┃ ┣ 📜 IIDCheck.java
┃ ┃ ┣ 📜 MIDCheck.java
┃ ┣ 📂 EvotingBooth/
┃ ┃ ┣ 📜 IEvotingBooth.java
┃ ┃ ┣ 📜 MEvotingBooth.java
┃ ┣ 📂 Logs/
┃ ┃ ┣ 📜 ILogs.java
┃ ┃ ┣ 📜 MLogs.java
┃ ┣ 📂 ExitPoll/
┃ ┃ ┣ 📜 IExitPoll.java
┃ ┃ ┣ 📜 MExitPoll.java
┃ ┣ 📜 IAll.java (Interface que combina todas as outras)
┃
┣ 📂 Threads/ (Contém as Threads e Interfaces das Threads)
┃ ┣ 📜 TVoter.java (Implementação do Voter)
┃ ┣ 📜 TPollClerk.java (Implementação do PollClerk)
┃ ┣ 📜 TPollster.java (Implementação do Pollster)
┃
┣ 📂 Main/ (Contém a classe Main para rodar o sistema)
┃ ┣ 📜 Main.java
```