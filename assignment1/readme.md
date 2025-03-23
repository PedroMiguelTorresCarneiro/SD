# Distributed Voting Simulation

This project is a simulation of a distributed voting system written in Java. It models a multi-threaded environment using monitors and threads, and provides a GUI to manage and visualize the voting process.

## Project Structure

> Note: The names below refer to Java packages, which match the directory structure under `/src`.

- `Main`: Main class and GUI interface for launching the simulation.
  - **Main.java**
  - **mainGUI.form**
  - **mainGUI.java**

- `Monitors`: Core logic of the system, structured into five components:
  - `EvotingBooth`: Handles voting interactions.
    - **IEVotingBooth_ALL.java**
    - **IEVotingBooth_TPollClerk.java**
    - **IEVotingBooth_TVoter.java**
    - **MEvotingBooth.java**
  - `ExitPoll`: Manages exit poll interviews.
    - **IExitPoll_ALL.java**
    - **IExitPoll_TPollClerk.java**
    - **IExitPoll_TPollster.java**
    - **IExitPoll_TVoter.java**
    - **MExitPoll.java**
  - `IDCheck`: Verifies voter identity.
    - **IIDCheck_ALL.java**
    - **IIDCheck_TVoter.java**
    - **MIDCheck.java**
  - `PollStation`: Coordinates access to the station.
    - **IPollStation_ALL.java**
    - **IPollStation_TPollClerk.java**
    - **IPollStation_TVoter.java**
    - **MPollStation.java**
  - `Repository`: Stores logs and system state.
    - **IRepo_ALL.java**
    - **IRepo_ExitPoll.java**
    - **IRepo_IDChek.java**
    - **IRepo_PollStation.java**
    - **IRepo_VotingBooth.java**
    - ** MRepo.java**

- `Threads`: Implements the behavior of participants.
  - **TPollClerk.java**
  - **TPollster.java**
  - **TVoter.java**

<br>

> Note: `/logs ` Directory where all the log files will be stored after runnnig the simulation.

- `/logs`: Example log files generated during simulation.
  - **log1.txt**
  - **log2.txt**

<br>

> Note: `/diagrams` Contains the diagrams of the project

- `/diagrams`: PDF file containing the system's **interaction diagram**.

<br>

> Note: `/dist/javadoc` Contains javadoc documentation

- `dist/javadoc`: Folder containing the **JavaDoc** documentation generated from the code.

## 🚀 How to Run

### On Linux/macOS

Execute the following script:

```bash
./runAssignment1.sh
```

This will compile all Java files into a `build` directory and run the simulation.

### On Windows

Run the following PowerShell script:

```powershell
.\runAssignment1.ps1
```

It will perform the same steps: compile and launch the application.

## 📄 Documentation

- [Interaction Diagram (PDF)](/diagrams/interactionDiagram.pdf): Contains the interaction diagram of the system.
- [JavaDoc](/dist/javadoc/index.html): Full documentation on interfaces, classes, and methods.

