package Monitors.Logs;

import java.util.concurrent.locks.ReentrantLock;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class MLogs implements ILogs {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    // Singleton instance
    private static MLogs instance;

    // Configuration values
    private final int votesNumber;
    private final int votersNumber;
    private final int fifoLimit;

    // Lock for thread safety
    private final ReentrantLock lock;

    // File writer for logging to a file
    private BufferedWriter fileWriter;

    // Private constructor
    private MLogs(int votesNumber, int votersNumber, int fifoLimit) {
        this.votesNumber = votesNumber;
        this.votersNumber = votersNumber;
        this.fifoLimit = fifoLimit;
        this.lock = new ReentrantLock();

        // Initialize the file writer
        try {
            fileWriter = new BufferedWriter(new FileWriter("./logs.txt", true)); // Append mode
        } catch (IOException e) {
            System.err.println("Failed to initialize file writer: " + e.getMessage());
        }

        logHeader();
    }

    // Singleton getInstance method
    public static MLogs getInstance(int votesNumber, int votersNumber, int fifoLimit) {
        if (instance == null) {
            instance = new MLogs(votesNumber, votersNumber, fifoLimit);
        }
        return instance;
    }

    // Helper method to write to both console and file
    private void writeLog(String message) {
        lock.lock(); // Acquire the lock
        try {
            // Print to console
            System.out.print(message);

            // Write to file
            if (fileWriter != null) {
                fileWriter.write(message.replaceAll("\u001B\\[[;\\d]*m", "")); // Remove ANSI codes for file
                fileWriter.flush(); // Ensure the message is written immediately
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Print the header with configuration details
    @Override
    public void logHeader() {
        lock.lock(); // Acquire the lock
        try {
            String header = CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    "Stop Condition       : " + YELLOW + votesNumber + RESET + " votes\n" +
                    "Max of Voters        : " + YELLOW + votersNumber + RESET + " threads\n" +
                    "PollStation Capacity : " + YELLOW + fifoLimit + RESET + " places\n" +
                    CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    RED + "Probabilities:" + RESET + "\n" +
                    "   - PARTY A wins              : " + YELLOW + "70%" + RESET + "\n" +
                    "   - Voter being chosen        : " + YELLOW + "10%" + RESET + "\n" +
                    "   - Voter answers             : " + YELLOW + "60%" + RESET + "\n" +
                    "   - Voter lies on survey      : " + YELLOW + "20%" + RESET + "\n" +
                    "   - Voter reborn a new ID     : " + YELLOW + "60%" + RESET + "\n" +
                    CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    RED + "LABEL:" + RESET + "\n" +
                    BOLD + "   - IDCheck:" + RESET + "\n" +
                    "      - ✔ : voter checked for voting\n" +
                    "      - ✖ : voter already voted\n" +
                    BOLD + "   - Voting:" + RESET + "\n" +
                    "      - Voter (A | B)  : refers to the Vote of the voter\n" +
                    BOLD + "   - Survey:" + RESET + "\n" +
                    "      - Voter L : voter lied on survey\n" +
                    CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    "| PollStation | Waiting | Inside | IDCheck | Voting | ExitPoll | Survey |\n" +
                    CYAN + "-------------------------------------------------------------------------" + RESET + "\n";

            writeLog(header);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the state of the poll station
    @Override
    public void logPollStation(String state) {
        lock.lock(); // Acquire the lock
        try {
            String coloredState = state.equals("OPEN  ") ? GREEN + state + RESET : RED + state + RESET;
            String logMessage = String.format("| %-11s      |         |        |         |        |          |        |%n", coloredState);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the voter in the Waiting column
    @Override
    public void logWaiting(String voterId) {
        lock.lock(); // Acquire the lock
        try {
            String logMessage = String.format("|             |   %s%-5s%s |        |         |        |          |        |%n", BOLD, voterId, RESET);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the voter in the Inside column
    @Override
    public void logInside(String voterId) {
        lock.lock(); // Acquire the lock
        try {
            String logMessage = String.format("|             |         |  %s%-5s%s |         |        |          |        |%n", GREEN, voterId, RESET);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the voter in the IDCheck column
    @Override
    public void logIDCheck(String voterId, char accepted) {
        lock.lock(); // Acquire the lock
        try {
            String color = (accepted == '✔') ? GREEN : RED;
            String logMessage = String.format("|             |         |        |  %s%-4s%c%s  |        |          |        |%n", color, voterId, accepted, RESET);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the voter in the Voting column
    @Override
    public void logVoting(String voterId, char vote) {
        lock.lock(); // Acquire the lock
        try {
            String voteColor = (vote == 'A') ? CYAN : RED;
            String logMessage = String.format("|             |         |        |         |  %-4s%s%c%s |          |        |%n", voterId, voteColor, vote, RESET);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the voter in the ExitPoll column
    @Override
    public void logExitPoll(String voterId) {
        lock.lock(); // Acquire the lock
        try {
            String logMessage = String.format("|             |         |        |         |        |   %s%-5s%s  |        |%n", YELLOW, voterId, RESET);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the voter in the Survey column
    @Override
    public void logSurvey(String voterId, char lieOrNot) {
        lock.lock(); // Acquire the lock
        try {
            String logMessage = String.format("|             |         |        |         |        |          |  %s%-4s%c%s |%n", BOLD, voterId, lieOrNot, RESET);
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the survey results
    public void logSurveyResults(long A, long B, String winner) {
        lock.lock(); // Acquire the lock
        try {
            String logMessage = CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    CYAN + "\n\n----------------------| SURVEY RESULTS" + RESET + "\n" +
                    String.format("Total votes for A: %s%d%s%n", YELLOW, A, RESET) +
                    String.format("Total votes for B: %s%d%s%n", YELLOW, B, RESET) +
                    BOLD + GREEN + " SURVEY WINNER -> " + winner + RESET + "\n" +
                    CYAN + "----------------------" + RESET + "\n";
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Log the election results
    public void logElectionResults(long A, long B, String winner) {
        lock.lock(); // Acquire the lock
        try {
            String logMessage = CYAN + "\n----------------------| ELECTION RESULTS" + RESET + "\n" +
                    String.format("Total votes for A: %s%d%s%n", YELLOW, A, RESET) +
                    String.format("Total votes for B: %s%d%s%n", YELLOW, B, RESET) +
                    BOLD + GREEN + "  WINNER  🏆🏆 -> " + winner + RESET + "\n" +
                    CYAN + "----------------------" + RESET + "\n";
            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Close the file writer when done
    public void close() {
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close file writer: " + e.getMessage());
        }
    }
}