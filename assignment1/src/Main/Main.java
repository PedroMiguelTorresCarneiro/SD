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

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int userArgs[] = GetUserArguments(args);

        int numVoters = userArgs[0];
        int maxCapacity = userArgs[1];
 
        int maxVotes = 20;    
    
        // Shared Regions
        IRepo_ALL logs = MRepo.getInstance(maxVotes, numVoters, maxCapacity);
        
        IPollStation_ALL pollStation = MPollStation.getInstance(maxCapacity, (IRepo_PollStation)logs);
        IIDCheck_ALL idCheck = MIDCheck.getInstance((IRepo_IDChek)logs);
        IEVotingBooth_ALL booth = MEvotingBooth.getInstance((IRepo_VotingBooth)logs);
        IExitPoll_ALL exitPoll = MExitPoll.getInstance((IRepo_ExitPoll)logs);
        
        
        // Threads 
        Thread pollClerk = null; 
        Thread pollster = null;
        Thread voter = null;
        
        pollClerk = new Thread(TPollClerk.getInstance((IPollStation_TPollClerk)pollStation, (IIDCheck_TPollClerk)idCheck, (IEVotingBooth_TPollClerk)booth, (IExitPoll_TPollClerk)exitPoll, maxVotes));
        pollster = new Thread(TPollster.getInstance((IExitPoll_TPollster)exitPoll));
        
        
        pollClerk.start();
        pollster.start();
              
        Thread[] voters = new Thread[numVoters];
        
        for (int i = 0; i < numVoters; i++) {
            voters[i] = new Thread(TVoter.getInstance("V" + (i+1), (IPollStation_TVoter) pollStation,(IIDCheck_TVoter) idCheck, (IEVotingBooth_TVoter) booth, (IExitPoll_TVoter) exitPoll));
            voters[i].start();
        }
        
        
        pollClerk.join();
        pollster.join();
        for (Thread v : voters){
            v.join();
        }

        System.out.println("✅ Election simulation finished!");        
        
    }

    private static int[] GetUserArguments(String args[]){
        if (args.length != 2){
            System.out.println("You must provide 2 arguments: the number of voters and the maximum capacity of the polling station's queue.");
            System.exit(1);
        }

        int numVoters;
        int maxCapacity;

        try {
            numVoters = Integer.parseInt(args[0]);
            maxCapacity = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Both arguments must be integers.");
            System.exit(1);
            return null; // This return is unreachable but added to satisfy the compiler.
        }

        if (numVoters < 3){
            numVoters = 3;
        }else if (numVoters > 10){
            numVoters = 10;
        }

        if (maxCapacity < 2){
            maxCapacity = 2;
        }else if (maxCapacity > 5){
            maxCapacity = 5;
        }

        return new int[]{numVoters, maxCapacity};
    }
}
