package Main;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.IDCheck.IIDCheck;
import Monitors.PollStation.IPollStation;
import Threads.TPollClerk;
import Threads.TPollster;
import Threads.TVoter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numVoters = 3;   // Número de eleitores a serem criados
        int maxCapacity = 2;  // Capacidade máxima dentro da PollStation
        int maxVotes = 6;    // Número total de votos antes de fechar a votação
           
        System.out.println("\n----------------------------------");
        System.out.println("Iniciar simulação de eleições...");
        System.out.println("Total de votos possíveis: " + maxVotes);
        System.out.println("Total de votantes: " + numVoters);
        System.out.println("----------------------------------");
        
        
        // Shared Regions
        IPollStation pollStation = IPollStation.getInstance(maxCapacity);
        IIDCheck idCheck = IIDCheck.getInstance();
        IEvotingBooth booth = IEvotingBooth.getInstance();
        IExitPoll exitPoll = IExitPoll.getInstance();
        
        // Threads 
        TPollClerk pollClerk = new TPollClerk(pollStation, idCheck, booth, exitPoll, maxVotes);
        TPollster pollster = new TPollster(exitPoll);
        
        pollClerk.start();
        pollster.start();
        
        
        for (int i = 1; i <= 10; i++) {
            new TVoter("V" + i, pollStation, booth, exitPoll).start();
        }

        pollClerk.join();
        pollster.join();

        System.out.println("✅ Simulação de eleições terminada!");
        System.exit(0); // Termina a execução

        
    }
}
