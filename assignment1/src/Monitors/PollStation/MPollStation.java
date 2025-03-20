package Monitors.PollStation;


import Monitors.IDCheck.IIDCheck;
import Monitors.Repository.MRepo;
import Threads.TPollClerk;
import Threads.TVoter;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Monitors.Repository.IRepo;

public class MPollStation implements IPollStation_ALL {
    private static MPollStation instance = null;
    private static MRepo log;
    private final int capacidadeMax;
    private Boolean alreadyClosed = false;
    
    private final Random random = new Random();

    private final ReentrantLock lock_changeState, lock_externalFifo, lock_exitingPS, lock_isEmpty, lock_maxedVotes, lock_isOpen;
    private final Condition simulate_Open,internalQueue;
    private int votersInside = 0;
    
    private static final LinkedList<String> externalQueue = new LinkedList<>();

    private enum PollStationState {
        CLOSED,
        OPEN ,
        CLOSED_AFTER
    }

    private PollStationState state = PollStationState.CLOSED;
    
    private MPollStation(int capacidadeMax, IRepo logs) {
        this.capacidadeMax = capacidadeMax;
        log = (MRepo) logs;
        
        lock_changeState = new ReentrantLock();
        simulate_Open = lock_changeState.newCondition();
        
        lock_externalFifo = new ReentrantLock(true);
        internalQueue = lock_externalFifo.newCondition();

        
        lock_exitingPS = new ReentrantLock();
        
        lock_isEmpty = new ReentrantLock();
        
        lock_maxedVotes = new ReentrantLock();
        
        lock_isOpen = new ReentrantLock();         
    }
    
    public static IPollStation_ALL getInstance(int capacidadeMax, IRepo logs) {
        if (instance == null) {
            instance =  new MPollStation(capacidadeMax, logs);
        }

        return instance;
    }

    public void openPS(TPollClerk pollclerk) throws InterruptedException {
        lock_changeState.lock();
        try{
            
            log.logPollStation("CLOSED");
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulate_Open.await(randomDuration, TimeUnit.MILLISECONDS);
            
           state = PollStationState.OPEN;
           
           log.logPollStation("OPEN  ");
           
           //System.out.println("The Clerk opened the PollStation");
           

            pollclerk.setState(TPollClerk.PollClerkState.ID_CHECK_WAIT);
        } finally {
            lock_changeState.unlock();
        }
    }
    
    public void enterPS(TVoter voter) throws InterruptedException {
        lock_externalFifo.lock();
        try{
            if(!externalQueue.contains(voter.getID())){
                externalQueue.add(voter.getID());
                log.logWaiting(voter.getID());
            }
            
            if (votersInside >= capacidadeMax || !state.equals(PollStationState.OPEN)){
               return;
            }

            votersInside++;
            
            while(true){
                // internal Queue
                //System.out.println("Voter " + voter.getID() + " is waiting inside");
                
                log.logInside(voter.getID());
                externalQueue.remove(voter.getID());
                internalQueue.await();

                break;        
            }

            voter.setState(TVoter.VoterState.WATING_INSIDE);
    
        } finally {
            lock_externalFifo.unlock();
        }
    }

    public void callNextVoter(TPollClerk pollclerk) {
        lock_externalFifo.lock();
        
        try {
            // deixa entrar uma pessoa porque já saiu alguem do fifo!

            if (votersInside == 0) {
                return;
            }

            internalQueue.signal(); 
            


            pollclerk.setState(TPollClerk.PollClerkState.ID_CHECK);
            

        } finally {
            lock_externalFifo.unlock();
        }
    }

    public void closePS() {
        lock_changeState.lock();

        try{
            state = PollStationState.CLOSED_AFTER;
            
            if(!alreadyClosed){
                log.logPollStation("CLOSED");
                alreadyClosed = true;
            }
            

        } finally {
            lock_changeState.unlock();
        }
    }

    public boolean isCLosedAfterElection() {
        lock_isOpen.lock();

        try{
            return state.equals(PollStationState.CLOSED_AFTER);
        } finally{
            lock_isOpen.unlock();
        }
    }

    

    public void exitingPS(TVoter voter) throws InterruptedException {
        // para deixar entrar uma pessoa
        lock_exitingPS.lock();

        try{
          votersInside--;
          
          voter.setState(TVoter.VoterState.EXIT_PS);
          
          log.logExitPoll(voter.getID());
        
        } finally {
            lock_exitingPS.unlock();
        }
    }
    
    public boolean isEmpty(){
        lock_isEmpty.lock();

        try{
            return votersInside == 0;
        } finally {
            lock_isEmpty.unlock();
        }
    }

    public boolean maxVotes(int maxVotes, int votersRegistered) {
        lock_maxedVotes.lock();

        try{
            if(votersRegistered >= maxVotes) {
                return true;
            }
            return false;
        
        } finally{
            lock_maxedVotes.unlock();
        }
    }

}
