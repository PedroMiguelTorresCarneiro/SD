package Main;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.IDCheck.IIDCheck;
import Monitors.Logs.ILogs;
import Monitors.PollStation.IPollStation;
import Threads.TPollClerk;
import Threads.TPollster;
import Threads.TVoter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numVoters = 10;   // Número de eleitores a serem criados
        int maxCapacity = 5;  // Capacidade máxima dentro da PollStation
        int maxVotes = 20;    // Número total de votos antes de fechar a votação
        
        /*
        System.out.println("\n----------------------------------");
        System.out.println("Starting election simulation...");
        System.out.println("Total possible votes: " + maxVotes);
        System.out.println("Total voters: " + numVoters);
        System.out.println("----------------------------------");
        */
        
        // Shared Regions
        ILogs logs = ILogs.getInstance(maxVotes, numVoters, maxCapacity);
        
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
        //System.exit(0); // Termina a execução
        
    }
}
