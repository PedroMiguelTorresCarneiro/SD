package Main;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.IDCheck.IIDCheck;
import Monitors.PollStation.IPollStation;
import Threads.TPollClerk;
import Threads.TPollster;
import Threads.TVoter;
import Monitors.Repository.IRepo;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int userArgs[] = GetUserArguments(args);

        int numVoters = userArgs[0];
        int maxCapacity = userArgs[1];
 
        int maxVotes = 20;    
    
        // Shared Regions
        IRepo logs = IRepo.getInstance(maxVotes, numVoters, maxCapacity);
        
        IPollStation pollStation = IPollStation.getInstance(maxCapacity, logs);
        IIDCheck idCheck = IIDCheck.getInstance(logs);
        IEvotingBooth booth = IEvotingBooth.getInstance(logs);
        IExitPoll exitPoll = IExitPoll.getInstance(logs);
        
        
        // Threads 
        TPollClerk pollClerk = new TPollClerk(pollStation, idCheck, booth, exitPoll, maxVotes);
        TPollster pollster = new TPollster(exitPoll);
        
        pollClerk.start();
        pollster.start();
              
        TVoter[] voters = new TVoter[numVoters];
        
        for (int i = 0; i < numVoters; i++) {
            voters[i] = new TVoter("V" + (i+1), pollStation, idCheck, booth, exitPoll);
            voters[i].start();
        }
        
        for (TVoter v : voters){
            v.join();
        }
        pollClerk.join();
        pollster.join();

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
