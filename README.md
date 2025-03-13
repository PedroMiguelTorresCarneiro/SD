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
┃ ┣ 📂 Voter/
┃ ┃ ┣ 📜 ITVoter.java (Interface do Voter)
┃ ┃ ┣ 📜 TVoter.java (Implementação do Voter)
┃ ┣ 📂 PollClerk/
┃ ┃ ┣ 📜 ITPollClerk.java (Interface do PollClerk)
┃ ┃ ┣ 📜 TPollClerk.java (Implementação do PollClerk)
┃ ┣ 📂 Pollster/
┃ ┃ ┣ 📜 ITPollster.java (Interface do Pollster)
┃ ┃ ┣ 📜 TPollster.java (Implementação do Pollster)
┃
┣ 📂 Main/ (Contém a classe Main para rodar o sistema)
┃ ┣ 📜 Main.java
```