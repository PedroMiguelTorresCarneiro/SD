package Main;

import stubs.EvotingBooth.IEVotingBooth_ALL;
import stubs.EvotingBooth.IEVotingBooth_TPollClerk;
import stubs.EvotingBooth.IEVotingBooth_TVoter;
import stubs.EvotingBooth.MEvotingBooth;

import stubs.ExitPoll.IExitPoll_ALL;
import stubs.ExitPoll.IExitPoll_TPollClerk;
import stubs.ExitPoll.IExitPoll_TPollster;
import stubs.ExitPoll.IExitPoll_TVoter;
import stubs.ExitPoll.MExitPoll;

import stubs.IDCheck.IIDCheck_ALL;
import stubs.IDCheck.IIDCheck_TVoter;
import stubs.IDCheck.MIDCheck;

import stubs.PollStation.IPollStation_ALL;
import stubs.PollStation.IPollStation_TPollClerk;
import stubs.PollStation.IPollStation_TVoter;
import stubs.PollStation.MPollStation;

import Threads.TPollClerk;
import Threads.TPollster;
import Threads.TVoter;

import stubs.Repository.IRepo_ALL;
import stubs.Repository.IRepo_PollStation;
import stubs.Repository.IRepo_IDChek;
import stubs.Repository.IRepo_ExitPoll;
import stubs.Repository.IRepo_VotingBooth;
import stubs.Repository.MRepo;

import javax.swing.SwingUtilities;

/**
 * The main class of the program is responsible for starting the simulation (creating the threads and shared regions) and initializing the GUI.
 * This class starts the election simulation's GUI and provides the method applied when the user clicks the "Start" button.
 * The method mentioned before includes the creation of voters, poll clerks, and pollsters, as well as the shared regions
 * such as the polling station, ID check, voting booth, and exit poll.
 * 
 * The class also ensures GUI responsiveness by running the simulation in a separate thread,
 * disabling the start button during simulation and re-enabling it once the process finishes.
 * 
 * @author David Palricas 
 * @author Inês Águia
 * @author Pedro Carneiro 
 */
public class Main {
    /**
     * The gui attribute stores a reference to the program's GUI object.
     */
    private static mainGUI gui;

    /**
     * The main method of the program is responsible for creating and displaying the GUI.
     * This method is the entry point of the application.
     * 
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gui = new mainGUI();
            gui.setVisible(true);
        });
    }

    /**
     * The startSimulation method is responsible for starting the simulation by creating the threads and shared regions.
     * This method is called by the GUI when the user clicks the "Start Simulation" button.
     * 
     * The method disables the "Start" button to avoid concurrent executions, runs the simulation logic
     * in a separate thread to prevent GUI freezing, and re-enables the button after simulation ends.
     * 
     * @param numVoters The number of voters participating in the simulation.
     * @param maxCapacity The maximum capacity of voters in the inside queue of the polling station.
     * @param maxVotes The maximum number of votes required to trigger the end of the election.
     */
    public static void startSimulation(int numVoters, int maxCapacity, int maxVotes) throws InterruptedException {
        gui.setStartButtonEnabled(false); // Disable button during simulation
        
        try {

            IRepo_ALL logs = MRepo.getInstance(maxVotes, numVoters, maxCapacity, gui);

            // Shared regions
            IPollStation_ALL pollStation = MPollStation.getInstance(maxCapacity, (IRepo_PollStation) logs);
            IIDCheck_ALL idCheck = MIDCheck.getInstance((IRepo_IDChek) logs);
            IEVotingBooth_ALL booth = MEvotingBooth.getInstance((IRepo_VotingBooth) logs);
            IExitPoll_ALL exitPoll = MExitPoll.getInstance((IRepo_ExitPoll) logs);      

            // Threads
            Thread pollClerk = new Thread(TPollClerk.getInstance(
                    (IPollStation_TPollClerk) pollStation,
                    (IEVotingBooth_TPollClerk) booth,
                    (IExitPoll_TPollClerk) exitPoll,
                    maxVotes));

            Thread pollster = new Thread(TPollster.getInstance((IExitPoll_TPollster) exitPoll));

            pollClerk.start();
            pollster.start();

            Thread[] voters = new Thread[numVoters];
            for (int i = 0; i < numVoters; i++) {
                voters[i] = new Thread(TVoter.getInstance(
                        "V" + (i + 1),
                        (IPollStation_TVoter) pollStation,
                        (IIDCheck_TVoter) idCheck,
                        (IEVotingBooth_TVoter) booth,
                        (IExitPoll_TVoter) exitPoll));
                voters[i].start();
            }

            pollClerk.join();
            pollster.join();
            
            for (Thread v : voters) {
                v.join();
            }

            System.out.println("✅ Election simulation finished!\n");

            MRepo.resetInstance();
            MPollStation.resetInstance();
            MIDCheck.resetInstance();
            MEvotingBooth.resetInstance();
            MExitPoll.resetInstance();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            gui.setStartButtonEnabled(true); // Re-enable button after simulation
        }
    }
}
