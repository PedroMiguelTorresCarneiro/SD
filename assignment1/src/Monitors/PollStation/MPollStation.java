package stubs.PollStation;

import stubs.Repository.MRepo;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import stubs.Repository.IRepo_PollStation;

/**
 * The MPollStation class implements the IPollStation_ALL interface and represents the polling station shared region.
 * The polling station shared region is responsible for managing the voters that are inside the polling station.
 * The polling station shared region is accessed by the poll clerk and the voters.
 * The poll clerk interacts with the polling station to open and close the polling station and to call the next voter.
 * The voters interact with the polling station to enter and exit the polling station.
 * The polling station shared region is implemented using the monitor pattern.
 * 
 * @see IPollStation_ALL
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 * 
 */
public class MPollStation implements IPollStation_ALL {
    /**
     * The instance attribute reprsents the singleton instance of the polling station shared region.
     */
    private static MPollStation instance = null;

    /**
     * The log attribute represents the repository shared region.
     * The polling station shared region shares information to the repository, 
     * to be logged (log file and on the terminal) and displayed in the GUI
     */
    private static IRepo_PollStation log;

    /**
     * The capacidadeMax attribute represents the maximum capacity of voters in
     * the pollstation's inside queue.
     */
    private final int capacidadeMax;

    /**
     * The alreadyClosed attribute represents a flag that indicates if the polling station
     * has already been closed after the election.
     */
    private Boolean alreadyClosed = false;
    
    /**
     * The random attribute represents a random number generator.
     */
    private final Random random = new Random();
    
    /**
     * The lockChangeState, lockExternalFifo, lockExitingPS, lockIsEmpty, 
     * lockMaxedVotes, and lockIsOpen attributes represent 
     * the locks used to control the access to the shared region.
     * Their lock funcionalities are described in their variable names.
     */
    private final ReentrantLock lockChangeState, lockExternalFifo, lockExitingPS, lockIsEmpty, lockMaxedVotes, lockIsOpen;

    /**
     * The internalQueue atribut represent lock conditions used to control the polling station inside queue.
     */
    private final Condition internalQueue;

    /**
     * The votersInside attribute represents the number of voters in the quue inside the polling station.
     */
    private int votersInside = 0;
    
    /**
     * The externalQueue attribute represents the queue of voters waiting outside the polling station.  
     */
    private final LinkedList<String> externalQueue = new LinkedList<>();
    
    /**
     * The PollStationState enum represents the possible states of the polling station shared region.
     */
    private enum PollStationState {
        /**
         * The CLOSED state represents the state where the polling station is closed.
         */
        CLOSED,
        /**
         * The OPEN state represents the state where the polling station is open.
         */
        OPEN ,
        /**
         * The CLOSED_AFTER state represents the state where the polling station is closed after the election.
         */
        CLOSED_AFTER
    }
    
    /**
     * The state attribute represents the current state of the polling station.
     */
    private PollStationState state = PollStationState.CLOSED;
    
    /**
     * The MPollStation constructor initializes the polling station shared region and its attributes.
     * 
     * @param capacidadeMax the maximum capacity of voters in the pollstation's inside queue
     * @param logs the repository shared region
     */
    private MPollStation(int capacidadeMax, IRepo_PollStation logs) {
        this.capacidadeMax = capacidadeMax;
        log = (MRepo) logs;
        
        lockChangeState = new ReentrantLock();
        
        lockExternalFifo = new ReentrantLock(true);
        internalQueue = lockExternalFifo.newCondition();

        
        lockExitingPS = new ReentrantLock();
        
        lockIsEmpty = new ReentrantLock();
        
        lockMaxedVotes = new ReentrantLock();
        
        lockIsOpen = new ReentrantLock();         
    }
    
    /**
     * The getInstance method returns the singleton instance of the polling station shared region.
     * If the polling station shared region has not been initialized, it initializes it.s
     * 
     * @param capacidadeMax the maximum capacity of voters in the pollstation's inside queue
     * @param logs the repository shared region
     * @return the singleton instance of the polling station shared region
     */
    public static IPollStation_ALL getInstance(int capacidadeMax, IRepo_PollStation logs) {
        if (instance == null) {
            instance =  new MPollStation(capacidadeMax, logs);
        }
        return instance;
    }

    /**
     * The openPs method simulates the opening of the polling station.
     * The polling station is opened for a random duration between 0.5s and 2s, its state is updated to OPEN,
     * and the repository is updated with the polling station state.
     * @throws InterruptedException if the thread is interrupted
     */
    @Override
    public void openPS() throws InterruptedException {
        lockChangeState.lock();

        try{      
            log.logPollStation("CLOSED");

            // Simulate voting with a random duration between 0.5s and 2s
            long randomDuration = 500 + random.nextInt(1501);
            //simulateOpen.await(randomDuration, TimeUnit.MILLISECONDS);
            Thread.sleep(randomDuration);

            state = PollStationState.OPEN;
           
            log.logPollStation("OPEN  ");
        
            //pollclerk.setState(TPollClerk.PollClerkState.ID_CHECK_WAIT);
        } finally {
            lockChangeState.unlock();
        }
    }
    
    /**
     * The canEnterPS method simulates the voters waiting outside and trying to enter the polling station.
     * A voter can only enter the polling station if there is space available and if the polling station is open.
     * When a voter enters the polling station, the voters enters in the polling station inside queue (votersIsinde varaible incremented).
     *  and waits for the poll clerk to call him to the ID check, this interaction is logged on the repository.
     * 
     * @param voterId the ID of the voter
     * @return true if the voter entered the polling station, false otherwise.
     * @throws InterruptedException if the thread is interrupted
     */
    @Override
    public boolean canEnterPS(String voterId) throws InterruptedException {
        lockExternalFifo.lock();

        try{
            // Adds a voter to the external queue
            if(!externalQueue.contains(voterId)){
                externalQueue.add(voterId);
                log.logWaiting(voterId);
            }
            
            if (votersInside >= capacidadeMax || !state.equals(PollStationState.OPEN)){
               return false;
            }

            votersInside++;
            
            while(true){          
                log.logInside(voterId);
                externalQueue.remove(voterId);
                internalQueue.await();

                return true;
            }    
        } finally {
            lockExternalFifo.unlock();
        }
    }
   
    @Override
    public boolean isPSclosedAfter(){
        lockExternalFifo.lock();
        
        try {
            return isEmpty() && state.equals(PollStationState.CLOSED_AFTER);
        } finally {
            lockExternalFifo.unlock();
        }
    }
    
    /**
     * The callNextVoter method simulates the poll clerk calling the next voter to the ID check.
     * The poll clerk can only call the next voter if there is a voter in the polling station inside queue.
     */
    @Override
    public void callNextVoter() {
        lockExternalFifo.lock();
        
        try {
            internalQueue.signal(); 
        } finally {
            lockExternalFifo.unlock();
        }
    }
    
    /**
     * The closePS method simulates the closing of the polling station (it is called by the poll clerk).
     * And updates the repository with the polling station state.
     */
    @Override
    public void closePS() {
        lockChangeState.lock();

        try{
            state = PollStationState.CLOSED_AFTER;
            
            // If the t polling station is already closed, it does not log again
            if(!alreadyClosed){
                log.logPollStation("CLOSED");
                alreadyClosed = true;
            }
        } finally {
            lockChangeState.unlock();
        }
    }
    
    /**
     * The isCLosedAfterElection method returns a flag that indicates if the polling station is closed after the election.
     * 
     * @return true if the polling station is closed after the election, false otherwise
     */
    @Override
    public boolean isCLosedAfterElection() {
        lockIsOpen.lock();

        try{
            return state.equals(PollStationState.CLOSED_AFTER);
        } finally{
            lockIsOpen.unlock();
        }
    }

    /**
     * The exitingPS method simulates the voter going to to the exit poll after voting.The repository updates the voter to show that he is in the exit poll and
     * the number of voters inside the polling station is decremented.
     * @param voterId the ID of the voter
     */
    @Override
    public void exitingPS(String voterId) throws InterruptedException {
        lockExitingPS.lock();

        try{
          votersInside--;
                    
          log.logExitPoll(voterId);
        
        } finally {
            lockExitingPS.unlock();
        }
    }
    
    /**
     * The isEmpty method returns a flag that indicates if the polling station inside queue is empty.
     * @return true if the polling station inside queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty(){
        lockIsEmpty.lock();

        try{
            return votersInside == 0;
        } finally {
            lockIsEmpty.unlock();
        }
    }
    
    /**
     * The maxVotes method returns a flag that indicates if the maximum number of votes has been reached.
     * @param maxVotes the maximum number of votes
     * @param votersRegistered the number of voters that have registered
     * @return true if the maximum number of votes has been reached, false otherwise
     */
    @Override
    public boolean maxVotes(int maxVotes, int votersRegistered) {
        lockMaxedVotes.lock();

        try{
            return votersRegistered >= maxVotes; 
        } finally{
            lockMaxedVotes.unlock();
        }
    }
    
    /**
    * Resets the singleton instance of the PollStation monitor.
    * This method is intended for infrastructure-level use only (e.g., Main),
    * to allow restarting the simulation cleanly.
    *
    * Should never be used by any thread (TVoter, TPollClerk, etc).
    */
   public static void resetInstance() {
       instance = null;
       log = null;
   }

}
