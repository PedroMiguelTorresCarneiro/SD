package Monitors.Repository;

import Main.mainGUI;
import java.util.concurrent.locks.ReentrantLock;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The MRepo class implements the IRepo_ALL interface and represents the repository .
 * The repository shared region logs the state of eached shared region, voters, stats from the survey and the election.
 * 
 * @see IRepo_ALL
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class MRepo implements IRepo_ALL {  
    /**
     * The RESET constant atributte is used to reset the color of the text in the terminal
     * Its the default color of the terminal
     */
    private static final String RESET = "\u001B[0m";

    /**
     * The RED constant atributte is used to change the color of the text in the terminal to red
     */
    private static final String RED = "\u001B[31m";

    /**
     * The GREEN constant atributte is used to change the color of the text in the terminal to green
     */
    private static final String GREEN = "\u001B[32m";

    /**
     * The YELLOW constant atributte is used to change the color of the text in the terminal to yellow
     */
    private static final String YELLOW = "\u001B[33m";

    /**
     * The CYAN constant atributte is used to change the color of the text in the terminal to cyan
     */
    private static final String CYAN = "\u001B[36m";

    /**
     * The BOLD constant atributte is used to change the style of the text in the terminal to bold
     */
    private static final String BOLD = "\u001B[1m";

    /**
     * The instance atributte is used to store the unique instance of the MRepo class
     */
    private static MRepo instance;

    /**
     * The votesNumber atributte is used to store the number of votes that are needed to end the election   
     */
    private final int votesNumber;

    /**
     * The votersNumber atributte is used to store the number of voters that are allowed to vote
     */
    private final int votersNumber;

    /**
     * The insideQueueMaxCapacity atributte is used to store the maximum capacity of the queue inside the polling station
     */
    private final int insideQueueMaxCapacity;

   /**
    * The lock atributte is used to store the lock that is used to synchronize the access to the shared region  
    */
    private final ReentrantLock lock;

    /**
     * The fileWriter atributte is used to store the reference to the file writer
     */
    private BufferedWriter fileWriter;
    
    /**
     * The gui atributte is used to store the reference to the simulation GUI
     */
    private mainGUI gui;

   /**
    * The MRepo constructor initializes a new MRepo object with the specified attributes.
    * It also initializes the file writer to log the state of the shared region and writes
    * the header with the configuration details.
    * 
    * @param votesNumber The number of votes that are needed to end the election
    * @param votersNumber The number of voters that are allowed to vote
    * @param insideQueueMaxCapacity The maximum capacity of the queue inside the polling station   
    * @param gui The simulation's GUI
    */
    private MRepo(int votesNumber, int votersNumber, int insideQueueMaxCapacity, mainGUI gui) {
        this.votesNumber = votesNumber;
        this.votersNumber = votersNumber;
        this.insideQueueMaxCapacity = insideQueueMaxCapacity;
        this.gui = gui;
        this.lock = new ReentrantLock();
    
        // Initialize the file writer with unique log file in /logs
        initializeLogFileWriter();

        logHeader();
    }

    /**
     * The getInstance method returns the unique instance of the MRepo class.
     * If the instance is null, a new MRepo object is created with the specified attributes.
     * 
     * @param votesNumber The number of votes that are needed to end the election
     * @param votersNumber The number of voters that are allowed to vote
     * @param insideQueueMaxCapacity The maximum capacity of the queue inside the polling station
     * @param gui The simulation's GUI
     * @return The unique instance of the MRepo class
     */
    public static IRepo_ALL getInstance(int votesNumber, int votersNumber, int insideQueueMaxCapacity, mainGUI gui) {
        if (instance == null) {
            instance = new MRepo(votesNumber, votersNumber, insideQueueMaxCapacity, gui);
        }

        return instance;
    }
    
    /**
     * The initializeLogFileWriter method initializes the file writer to log the state of the shared region.
     * If the file writer is not null, the method creates a new log file in the /logs directory.
     */
    private void initializeLogFileWriter() {
        String baseName = "log";
        String extension = ".txt";
        var logsDir = Paths.get("logs");
        int index = 0;
        Path logFilePath;

        try {
            if (!Files.exists(logsDir)) {
                Files.createDirectories(logsDir);
            }

            // Find the next available log file name
            do {
                String fileName = (index == 0) ? baseName + extension : baseName + index + extension;
                logFilePath = logsDir.resolve(fileName);
                index++;
            } while (Files.exists(logFilePath));

            // Create and assign the BufferedWriter
            fileWriter = new BufferedWriter(new FileWriter(logFilePath.toFile(), false)); // overwrite just in case
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
            fileWriter = null;
        }
    }

    
    
    /**
     * The writeLog method writes the specified message to the terminal and the log file.
     * 
     * @param message The message to be written
     */
    private void writeLog(String message) {
        lock.lock();

        try {
            // Print to the terminal
            System.out.print(message);

            if (fileWriter != null) {
                // Remove ANSI codes for file
                fileWriter.write(message.replaceAll("\u001B\\[[;\\d]*m", ""));
                fileWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }


   /**
    * The logHeader method  creates the header of the log file with the configuration details.
    * After writing the header, the writeLog method is called to write the message to the terminal and in the log file.    
    */
    public void logHeader() {
        lock.lock(); 

        try {
            String header = CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    "Stop Condition       : " + YELLOW + votesNumber + RESET + " votes\n" +
                    "Max of Voters        : " + YELLOW + votersNumber + RESET + " threads\n" +
                    "PollStation Capacity : " + YELLOW + insideQueueMaxCapacity + RESET + " places\n" +
                    CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    RED + "Probabilities:" + RESET + "\n" +
                    "   - PARTY A wins              : " + YELLOW + "70%" + RESET + "\n" +
                    "   - Voter being chosen        : " + YELLOW + "50%" + RESET + "\n" +
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
            lock.unlock(); 
        }
    }

   /**
    * The logPollStation method shows th state of the polling station in the GUI and logs the state of the polling station in the log file.
    * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.

    * @param state The state of the polling station (OPEN or CLOSED)
    */
    @Override
    public void logPollStation(String state) {
        lock.lock();

        try {

            gui.logPOLLSTATION(state);

            String coloredState = state.equals("OPEN  ") ? GREEN + state + RESET : RED + state + RESET;
            String logMessage = String.format("| %-11s      |         |        |         |        |          |        |%n", coloredState);

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The logWaiting method shows the voter waiting in queue outsaide the pollstation in the GUI and logs the voters in this queue.
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param voterId The voter's ID
     */
    @Override
    public void logWaiting(String voterId) {
        lock.lock(); 

        try {
            gui.addExternalFIFO(voterId);

            String logMessage = String.format("|             |   %s%-5s%s |        |         |        |          |        |%n", BOLD, voterId, RESET);

            writeLog(logMessage);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

   /**
    * The logInside method shows the voter waiting in queue inside the pollstation in the GUI and logs the voters in this queue.
    * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.

    * @param voterId The voter's ID
    */
    @Override
    public void logInside(String voterId) {
        lock.lock(); 

        try {
            gui.removeExternalFIFO(voterId);
            gui.addInternalFIFO(voterId);
            
            String logMessage = String.format("|             |         |  %s%-5s%s |         |        |          |        |%n", GREEN, voterId, RESET);

            writeLog(logMessage);
        } finally {
            lock.unlock();
        }
    }

    /**
     * The logIDCheck method shows the voter state of the voter after its ID was checked in the GUI and logs the voters in this queue.  
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param voterId The voter's ID
     * @param accepted The result of the ID check (✔ or ✖)
     */
    @Override
    public void logIDCheck(String voterId, char accepted) {
        lock.lock(); 

        try {
            
            gui.removeInternalFIFO(voterId);
            gui.logIDCHECK(voterId+accepted, accepted);
            gui.addIdcheckFIFO(voterId);
            
            String color = (accepted == '✔') ? GREEN : RED;
            String logMessage = String.format("|             |         |        |  %s%-4s%c%s  |        |          |        |%n", color, voterId, accepted, RESET);

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The logVoting method shows the voter state of the voter after its vote was cast in the GUI and logs the voters in this queue.
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param voterId The voter's ID
     * @param vote The party the voter voted for (A or B)
     */
    @Override
    public void logVoting(String voterId, char vote) {
        lock.lock();

        try {
            gui.logVOTING(voterId);

            String voteColor = (vote == 'A') ? CYAN : RED;
            String logMessage = String.format("|             |         |        |         |  %-4s%s%c%s |          |        |%n", voterId, voteColor, vote, RESET);

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The logExitPoll method shows the voters in the exit poll in the GUI and logs the voters in this poll.
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param voterId The voter's ID
     */
    @Override
    public void logExitPoll(String voterId) {
        lock.lock();

        try {
            gui.removeIdcheckFIFO(voterId);

            String logMessage = String.format("|             |         |        |         |        |   %s%-5s%s  |        |%n", YELLOW, voterId, RESET);

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The logSurvey method shows the voters in the survey in the GUI and logs the voters in this survey.
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param voterId The voter's ID
     * @param lieOrNot The result of the survey (L or not)
     */
    @Override
    public void logSurvey(String voterId, char lieOrNot) {
        lock.lock(); 

        try {
            gui.logSURVEY(voterId);

            String logMessage = String.format("|             |         |        |         |        |          |  %s%-4s%c%s |%n", BOLD, voterId, lieOrNot, RESET);

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The logStats method shows the stats of the survey in the GUI and logs the stats of the survey.
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param A The number of votes for party A
     * @param B The number of votes for party B
     * @param winner The winner of the survey
     */
    @Override
    public void logSurveyResults(long A, long B, String winner) {
        lock.lock(); 

        try {
            
            double partyARatio = (A / (double)(A + B)) * 100; 
            int partyAPercentage = (int) partyARatio; 

            double partyBRatio = (B / (double)(A + B)) * 100; 
            int partyBPercentage = (int) partyBRatio;
            
            gui.updatePartyA_survey(partyAPercentage);
            gui.updatePartyB_survey(partyBPercentage);
            
            if(partyAPercentage > partyBPercentage){
                gui.setSurveyPartyAwinner();
            }else if(partyAPercentage < partyBPercentage){
                gui.setSurveyPartyBwinner();
            }else{
                gui.setSurveyTie();
            }
            
            String logMessage = CYAN + "-------------------------------------------------------------------------" + RESET + "\n" +
                    CYAN + "\n\n----------------------| SURVEY RESULTS" + RESET + "\n" +
                    String.format("Total votes for A: %s%d%s%n", YELLOW, A, RESET) +
                    String.format("Total votes for B: %s%d%s%n", YELLOW, B, RESET) +
                    BOLD + GREEN + " SURVEY WINNER -> " + winner + RESET + "\n" +
                    CYAN + "----------------------" + RESET + "\n";

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The logElectionResults method shows the results of the election in the GUI and logs the results of the election.
     * After writing the message, the writeLog method is called to write the message to the terminal and in the log file.
     * 
     * @param A The number of votes for party A
     * @param B The number of votes for party B
     * @param winner The winner of the election
     */
    @Override
    public void logElectionResults(long A, long B, String winner) {
        lock.lock(); 

        try {
            // Clears the GUI
            gui.clean();

            double partyARatio = (A / (double)(A + B)) * 100; 
            int partyAPercentage = (int) partyARatio; 

            double partyBRatio = (B / (double)(A + B)) * 100; 
            int partyBPercentage = (int) partyBRatio;
            
            if(partyAPercentage > partyBPercentage){
                gui.setElecPartyAwinner();
            }else if(partyAPercentage < partyBPercentage){
                gui.setElecPartyBwinner();
            }else{
                gui.setElecTie();
            }
            
            gui.updatePartyA(partyAPercentage);
            gui.updatePartyB(partyBPercentage);
            
            String logMessage = CYAN + "\n----------------------| ELECTION RESULTS" + RESET + "\n" +
                    String.format("Total votes for A: %s%d%s%n", YELLOW, A, RESET) +
                    String.format("Total votes for B: %s%d%s%n", YELLOW, B, RESET) +
                    BOLD + GREEN + "  WINNER  🏆🏆 -> " + winner + RESET + "\n" +
                    CYAN + "----------------------" + RESET + "\n";

            writeLog(logMessage);
        } finally {
            lock.unlock(); 
        }
    }

    /**
     * The close method closes the file writer, and catches any IOException that may occur.
     */
    @Override
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