package Monitors.EvotingBooth;

import Monitors.IAll;

import Threads.TPollClerk;
import Threads.TVoter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MEvotingBooth implements IEvotingBooth{
    private static MEvotingBooth instance;
    private final Map<String, String> votes = new HashMap<>();
    private final ReentrantLock lock_gathering, lock_gettingVote, lock_vote;
    private final Condition simulateCountig, simulateVoting;
    private long countA, countB;
    private final double partyA_ratio = 0.6;
    private final Random random = new Random();
    
    private MEvotingBooth() {
        lock_gathering = new ReentrantLock();
        simulateCountig = lock_gathering.newCondition();
        
        lock_gettingVote = new ReentrantLock();
        
        lock_vote = new ReentrantLock();
        simulateVoting = lock_gathering.newCondition();
    }

    public static IEvotingBooth getInstance() {
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
            simulateCountig.await(2000, TimeUnit.MILLISECONDS);
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

    public void vote(TVoter voter) throws InterruptedException {
        lock_vote.lock();
        try{
            
            // Determinar o voto baseado na percentagem
            String vote = Math.random() < partyA_ratio ? "A" : "B";
            //Registar o voto
            votes.put(voter.getID(), vote);
            
            System.out.println("Eleitor " + voter.getID() + " votou em " + vote);
            
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulateVoting.await(randomDuration, TimeUnit.MILLISECONDS);
            
            // Mudar o State
            voter.setState(TVoter.VoterState.VOTING);
            
        } finally {
            lock_vote.unlock();
        }
    }

    public String getVote(String voterId) {
        lock_gettingVote.lock();
        try{
            return votes.get(voterId);
        } finally {
            lock_gettingVote.unlock();
        }
    }

    
}
