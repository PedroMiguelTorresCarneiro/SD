package Monitors.EvotingBooth;

import Monitors.IAll;

import Threads.TPollClerk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MEvotingBooth implements IAll{
    private static MEvotingBooth instance = null;
    private final Map<String, String> votes = new HashMap<>();
    private final ReentrantLock lock_gathering;
    private final Condition simulateCountig;
    private long countA, countB;
    
    private MEvotingBooth() {
        lock_gathering = new ReentrantLock();
        simulateCountig = lock_gathering.newCondition();
    }

    public static IAll getInstance() {
        if (instance == null) {
            instance = new MEvotingBooth();
        }
        return instance;
    }

    public void gathering() throws InterruptedException {
        lock_gathering.lock();
        try {
            System.out.println("Iniciando contagem dos votos...");

            // Contar os votos
            countA = votes.values().stream()
                    .filter(valor -> "A".equals(valor))
                    .count();

            countB = votes.values().stream()
                    .filter(valor -> "B".equals(valor))
                    .count();

            System.out.println("Contagem finalizada - Candidato A: " + countA + ", Candidato B: " + countB);

            // Sinalizar que a contagem foi concluída
            simulateCountig.await(1000, TimeUnit.MILLISECONDS);
        } finally {
            lock_gathering.unlock();
        }
    }

    public void publishElectionResults(TPollClerk pollClerk) {
        
        System.out.println("Total de votos em A: " + countA);
        System.out.println("Total de votos em B: " + countB);
        System.out.println("\n\n *VENCEDOR* -> "+ (countA > countB ? "A" : (countB > countA ? "B" : "EMPATE")) );
        
        pollClerk.setState(TPollClerk.PollClerkState.PUBLISHING_WINNER);
        
    }

    public void vote() {
        
    }

    public String getVote(String voterId) {
        return votes.get(voterId);
    }

    
}
