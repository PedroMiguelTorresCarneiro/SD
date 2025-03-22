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

/**
 * Class MEvotingBooth implements IEVotingBooth_ALL interface with the methods necessary to simulate the voting booth.
 * The class is responsible for the synchronization of the threads and the access to the shared memory.
 * The class has the following attributes:
 * - votes: a map with the votes of the voters;
 * - countA: the number of votes for party A;
 * - countB: the number of votes for party B;
 * - partyA_ratio: the ratio of votes for party A;
 * - random: a random number generator;
 * - lock_gathering: a reentrant lock for the gathering method;
 * - simulateCountig: a condition for the gathering method;
 * - lockSize: a reentrant lock for the getSize method;
 * - lock_gettingVote: a reentrant lock for the getVote method;
 * - lock_vote: a reentrant lock for the vote method;
 * - simulateVoting: a condition for the vote method.
 */
public class MEvotingBooth implements IEVotingBooth_ALL{
    private static MEvotingBooth instance;
    private static IRepo_VotingBooth log;
    private final Map<String, Character> votes = new HashMap<>();
    private final ReentrantLock lock_gathering, lock_gettingVote, lock_vote, lockSize;
    private final Condition simulateCountig, simulateVoting;
    private long countA, countB;
    private final double partyA_ratio = 0.7;
    private final Random random = new Random();
    
    /**
     * Constructor for MEvotingBooth.
     * @param logs repository of the voting booth.
     */
    private MEvotingBooth(IRepo_VotingBooth logs) {
        log = logs;
        lock_gathering = new ReentrantLock();
        simulateCountig = lock_gathering.newCondition();

        lockSize = new ReentrantLock();
        
        lock_gettingVote = new ReentrantLock();
        
        lock_vote = new ReentrantLock();
        simulateVoting = lock_vote.newCondition();
    }

    /**
     * Method to get the instance of MEvotingBooth.
     * @param logs repository of the voting booth.
     * @return instance of MEvotingBooth.
     */
    public static IEVotingBooth_ALL getInstance(IRepo_VotingBooth logs) {
        if (instance == null) {
            instance = new MEvotingBooth(logs);
        }
        return instance;
    }

    /**
     * Method to simulate the gathering of the votes.
     * @throws InterruptedException exception.
     */
    @Override
    public void gathering() throws InterruptedException {
        lock_gathering.lock();
        try {
            countA = votes.values().stream()
                    .filter(valor -> valor == 'A')
                    .count();

            countB = votes.values().stream()
                    .filter(valor -> valor == 'B')
                    .count();

            simulateCountig.await(2000, TimeUnit.MILLISECONDS);

        } finally {
            lock_gathering.unlock();
        }
    }

    /**
     * Method to publish the election results.
     * @param pollClerk poll clerk thread.
     */
    @Override
    public void publishElectionResults(TPollClerk pollClerk) {
        log.logElectionResults(countA, countB, (countA > countB ? "A" : (countB > countA ? "B" : "TIE")));

        pollClerk.setState(TPollClerk.PollClerkState.PUBLISHING_WINNER);
        log.close();
    }

    /**
     * Method to simulate the vote of a voter.
     * @param voter voter thread.
     * @throws InterruptedException exception.
     */
    @Override
    public void vote(TVoter voter) throws InterruptedException {
        lock_vote.lock();

        try{      
            Character vote = Math.random() < partyA_ratio ? 'A' : 'B';
        
            long randomDuration = 500 + random.nextInt(1501);
            simulateVoting.await(randomDuration, TimeUnit.MILLISECONDS);

            votes.put(voter.getID(), vote);

            log.logVoting(voter.getID(), vote);
            
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
