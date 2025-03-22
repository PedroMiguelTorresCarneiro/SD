package Main;

import Monitors.EvotingBooth.IEVotingBooth_ALL;
import Monitors.EvotingBooth.IEVotingBooth_TPollClerk;
import Monitors.EvotingBooth.IEVotingBooth_TVoter;
import Monitors.EvotingBooth.MEvotingBooth;

import Monitors.ExitPoll.IExitPoll_ALL;
import Monitors.ExitPoll.IExitPoll_TPollClerk;
import Monitors.ExitPoll.IExitPoll_TPollster;
import Monitors.ExitPoll.IExitPoll_TVoter;
import Monitors.ExitPoll.MExitPoll;

import Monitors.IDCheck.IIDCheck_ALL;
import Monitors.IDCheck.IIDCheck_TPollClerk;
import Monitors.IDCheck.IIDCheck_TVoter;
import Monitors.IDCheck.MIDCheck;

import Monitors.PollStation.IPollStation_ALL;
import Monitors.PollStation.IPollStation_TPollClerk;
import Monitors.PollStation.IPollStation_TVoter;
import Monitors.PollStation.MPollStation;

import Threads.TPollClerk;
import Threads.TPollster;
import Threads.TVoter;

import Monitors.Repository.IRepo_ALL;
import Monitors.Repository.IRepo_PollStation;
import Monitors.Repository.IRepo_IDChek;
import Monitors.Repository.IRepo_ExitPoll;
import Monitors.Repository.IRepo_VotingBooth;
import Monitors.Repository.MRepo;

import javax.swing.SwingUtilities;

public class Main {
    private static mainGUI gui;

    public static void startSimulation(int numVoters, int maxCapacity, int maxVotes) throws InterruptedException {
        //int maxVotes = 20;

        // Shared Regions
        IRepo_ALL logs = MRepo.getInstance(maxVotes, numVoters, maxCapacity, gui);

        IPollStation_ALL pollStation = MPollStation.getInstance(maxCapacity, (IRepo_PollStation) logs);
        IIDCheck_ALL idCheck = MIDCheck.getInstance((IRepo_IDChek) logs);
        IEVotingBooth_ALL booth = MEvotingBooth.getInstance((IRepo_VotingBooth) logs);
        IExitPoll_ALL exitPoll = MExitPoll.getInstance((IRepo_ExitPoll) logs);

        // Threads
        Thread pollClerk = null;
        Thread pollster = null;
        Thread voter = null;

        pollClerk = new Thread(TPollClerk.getInstance((IPollStation_TPollClerk) pollStation, (IIDCheck_TPollClerk) idCheck, (IEVotingBooth_TPollClerk) booth, (IExitPoll_TPollClerk) exitPoll, maxVotes));
        pollster = new Thread(TPollster.getInstance((IExitPoll_TPollster) exitPoll));

        pollClerk.start();
        pollster.start();

        Thread[] voters = new Thread[numVoters];

        for (int i = 0; i < numVoters; i++) {
            voters[i] = new Thread(TVoter.getInstance("V" + (i + 1), (IPollStation_TVoter) pollStation, (IIDCheck_TVoter) idCheck, (IEVotingBooth_TVoter) booth, (IExitPoll_TVoter) exitPoll));
            voters[i].start();
        }

        pollClerk.join();
        pollster.join();
        for (Thread v : voters) {
            v.join();
        }

        System.out.println("✅ Election simulation finished!");
    }

    public static void main(String[] args) {
        // Create and display the GUI
        SwingUtilities.invokeLater(() -> {
            gui = new mainGUI();
            gui.setVisible(true);
        });
    }
}