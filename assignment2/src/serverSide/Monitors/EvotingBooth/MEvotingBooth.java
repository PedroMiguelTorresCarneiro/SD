package stubs.EvotingBooth;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import stubs.Repository.IRepo_VotingBooth;

/**
 * The MEvotingBooth class implements the IEVotingBooth_ALL interface and represents the evoting booth shared region.
 * The evooting booth shared region is responsible for the voter to vote and 
 * the poll clerk to gather and count all the votes and also publish the election results.
 * 
 * @see STEVotingBooth_ALL
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class MEvotingBooth implements STEVotingBooth_ALL{
    /**
     * The instance atributte represents the singleton instance of the MEvotingBooth.
     */
    private static MEvotingBooth instance;

    /**
     * The log atributte represents the repository shared region.
     * The evoting booth shared region shares information to the repository
     * to be logged (log file and on the terminal) and displayed in the GUI.
     */
    private static IRepo_VotingBooth log;

    /**
     * The votes atributte represents the set of the votes.
     * The votes are stored in a map where the key is the voter ID and the value is the vote.
     */
    private final Map<String, Character> votes = new HashMap<>();
    
    /**
     * The lockGathering, lockGettingVote, lockVote and lockSize atributtes represent 
     * the locks for the gathering, getting vote, vote and size operations, respectively.
     */
    private final ReentrantLock lockGathering, lockGettingVote, lockVote, lockSize;

    /**
     * The simulateCountig and simulateVoting atributtes represent the lock conditions 
     * for the gathering and voting operations, respectively.
     */
    private final Condition simulateCountig, simulateVoting;

    /**
     * The countA and countB atributtes represent the number of votes for party A and party B, respectively.
     */
    private long countA, countB;

   /**
    * The PARTY_A_PROB constant atributte represents the probability of a voter to vote in party A.
    */
    private final static double PARTY_A_PROB = 0.7;

    /**
     * The random atributte represents the random number generator.
     */
    private final Random random = new Random();
    
    
    /**
     * The MEvotingBooth constructor initializes the evoting booth shared region and its atributtes.
     * 
     * @param logs the repository shared region.
     */
    private MEvotingBooth(IRepo_VotingBooth logs) {
        log = logs;
        lockGathering = new ReentrantLock();
        simulateCountig = lockGathering.newCondition();

        lockSize = new ReentrantLock();
        
        lockGettingVote = new ReentrantLock();
        
        lockVote = new ReentrantLock();
        simulateVoting = lockVote.newCondition();
    }

    /**
     * The getInstance method returns the singleton instance of the MEvotingBooth.
     * If the instance is null, a new instance is created.
     * 
     * @param logs repository of the voting booth.
     * @return instance of MEvotingBooth.
     */
    public static STEVotingBooth_ALL getInstance(IRepo_VotingBooth logs) {
        if (instance == null) {
            instance = new MEvotingBooth(logs);
        }
        return instance;
    }

    /**
     * The gathering method is called by the poll clerk to gather all the votes 
     * and count them.
     * A time delay is created to simulate the gathering of the votes.
     * 
     * @throws InterruptedException exception.
     */
    @Override
    public void gathering() throws InterruptedException {
        lockGathering.lock();

        try {
            countA = votes.values().stream()
                    .filter(valor -> valor == 'A')
                    .count();

            countB = votes.values().stream()
                    .filter(valor -> valor == 'B')
                    .count();

            simulateCountig.await(2000, TimeUnit.MILLISECONDS);

        } finally {
            lockGathering.unlock();
        }
    }

    /**
     * The publishElectionResults method is called by the poll clerk to publish the election results.
     * The election results are logged in the repository.
     */
    @Override
    public void publishElectionResults() {
        log.logElectionResults(countA, countB, (countA > countB ? "A" : (countB > countA ? "B" : "TIE")));
        log.close();
    }

    /**
     * The vote method is called by the voter to vote in a party.
     * A time delay is created to simulate the voting process.
     * The vote is stored in the votes map and is logged in the repository.
     * 
     * @param voterId the voter ID.
     * @throws InterruptedException exception.
     */
    @Override
    public void vote(String voterId) throws InterruptedException {
        lockVote.lock();

        try{      
            Character vote = Math.random() < PARTY_A_PROB ? 'A' : 'B';
        
            long randomDuration = 500 + random.nextInt(1501);
            simulateVoting.await(randomDuration, TimeUnit.MILLISECONDS);

            votes.put(voterId, vote);

            log.logVoting(voterId, vote);            
        } finally {
            lockVote.unlock();
        }
    }
   
    /**
     * The getVote method is called by the voter to get its vote.
     * 
     * @param voterId voter ID.
     * @return the vote of the voter.
     */
    @Override
    public Character getVote(String voterId) {
        lockGettingVote.lock();
        
        try{
            return votes.get(voterId);
        } finally {
            lockGettingVote.unlock();
        }
    }
    
    /**
     * The getVotesCount method returns the number of votes.
     * 
     * @return the number of votes.
     */
    @Override
    public int getVotesCount(){
        lockSize.lock();

        try{
            return votes.size();
        } finally {
            lockSize.unlock();
        }
    }
    
    /**
    * Resets the singleton instance of the MEvotingBooth monitor.
    * This method is intended solely for infrastructure-level use (e.g., from Main)
    * to allow the simulation to be restarted cleanly.
    *
    * Should never be used by any functional thread (TVoter, TPollClerk, etc).
    */
   public static void resetInstance() {
       instance = null;
       log = null;
   }

}
