package Monitors.EvotingBooth;

import Threads.TPollClerk;
import Threads.TVoter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Monitors.Repository.IRepo_VotingBooth;

public class MEvotingBooth implements IEVotingBooth_ALL{
    private static MEvotingBooth instance;
    private static IRepo_VotingBooth log;
    private final Map<String, Character> votes = new HashMap<>();
    private final ReentrantLock lock_gathering, lock_gettingVote, lock_vote, lockSize;
    private final Condition simulateCountig, simulateVoting;
    private long countA, countB;
    private final double partyA_ratio = 0.7;
    private final Random random = new Random();
    
    private MEvotingBooth(IRepo_VotingBooth logs) {
        log = logs;
        lock_gathering = new ReentrantLock();
        simulateCountig = lock_gathering.newCondition();

        lockSize = new ReentrantLock();
        
        lock_gettingVote = new ReentrantLock();
        
        lock_vote = new ReentrantLock();
        simulateVoting = lock_vote.newCondition();
    }

    public static IEVotingBooth_ALL getInstance(IRepo_VotingBooth logs) {
        if (instance == null) {
            instance = new MEvotingBooth(logs);
        }
        return instance;
    }

    @Override
    public void gathering() throws InterruptedException {
        lock_gathering.lock();
        try {
            //System.out.println("Starting vote counting...");

            // Contar os votos
            countA = votes.values().stream()
                    .filter(valor -> valor == 'A')
                    .count();

            countB = votes.values().stream()
                    .filter(valor -> valor == 'B')
                    .count();

       
            // Sinalizar que a contagem foi concluída
            simulateCountig.await(2000, TimeUnit.MILLISECONDS);

        } finally {
            lock_gathering.unlock();
        }
    }

    @Override
    public void publishElectionResults(TPollClerk pollClerk) {
        log.logElectionResults(countA, countB, (countA > countB ? "A" : (countB > countA ? "B" : "TIE")));

        pollClerk.setState(TPollClerk.PollClerkState.PUBLISHING_WINNER);
        log.close();
    }

    @Override
    public void vote(TVoter voter) throws InterruptedException {
        lock_vote.lock();

        try{      
            // Determinar o voto baseado na percentagem
            Character vote = Math.random() < partyA_ratio ? 'A' : 'B';
        
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulateVoting.await(randomDuration, TimeUnit.MILLISECONDS);

            //Registar o voto
            votes.put(voter.getID(), vote);

            //System.out.println("Voter " + voter.getID() + " voted for " + vote);
            log.logVoting(voter.getID(), vote);
            
            
            // Mudar o State
            voter.setState(TVoter.VoterState.VOTING);
            
        } finally {
            lock_vote.unlock();
        }
    }

    @Override
    public Character getVote(String voterId) {
        lock_gettingVote.lock();
        
        try{
            return votes.get(voterId);
        } finally {
            lock_gettingVote.unlock();
        }
    }
    
    @Override
    public int getSize(){
        lockSize.lock();

        try{
            return votes.size();
        } finally {
            lockSize.unlock();
        }
    }
}
