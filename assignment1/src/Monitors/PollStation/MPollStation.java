package Monitors.PollStation;


import Monitors.IDCheck.IIDCheck;
import Threads.TPollClerk;
import Threads.TVoter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MPollStation implements IPollStation {
    private static MPollStation instance = null;
    private final int capacidadeMax;
    
    private final Random random = new Random();
    private boolean open = false;
    private final ReentrantLock lock_changeState, lock_externalFifo, lock_CallingVoter, lock_exitingPS, lock_isEmpty, lock_maxedVotes, lock_isOpen;
    private final Condition simulate_Open, externalQueue, internalQueue;
    private int voterInside = 0;
    

    private MPollStation(int capacidadeMax) {
        this.capacidadeMax = capacidadeMax;
        
        lock_changeState = new ReentrantLock();
        simulate_Open = lock_changeState.newCondition();
        
        lock_externalFifo = new ReentrantLock(true);
        externalQueue = lock_externalFifo.newCondition();
        internalQueue = lock_externalFifo.newCondition();
                       
        lock_CallingVoter = new ReentrantLock();
        
        lock_exitingPS = new ReentrantLock();
        
        lock_isEmpty = new ReentrantLock();
        
        lock_maxedVotes = new ReentrantLock();
        
        lock_isOpen = new ReentrantLock();
               
    }
    
    public static IPollStation getInstance(int capacidadeMax) {
        if (instance == null) {
            instance =  new MPollStation(capacidadeMax);
        }
        return instance;
    }

    @Override
    public void openPS(TPollClerk pollclerk) throws InterruptedException {
        lock_changeState.lock();
        try{
            
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulate_Open.await(randomDuration, TimeUnit.MILLISECONDS);
            
            open = true;
            
            pollclerk.setState(TPollClerk.PollClerkState.ID_CHECK_WAIT);
        } finally {
            lock_changeState.unlock();
        }
    }
    
    @Override
    public void enterPS(TVoter voter) throws InterruptedException {
        lock_externalFifo.lock();
        try{
            
            while(voterInside >= capacidadeMax || !open){
                // caso não cumpra fica em espera!!!
                externalQueue.await();
            }
            voterInside++;
            
            while(true){
                // internal Queue
                internalQueue.await();
                voter.setState(TVoter.VoterState.WATING_INSIDE);
            }
    
        } finally {
            lock_externalFifo.unlock();
        }
    }

    @Override
    public void callNextVoter(TPollClerk pollclerk) {
        lock_CallingVoter.lock();
        try {
            // deixa entrar uma pessoa porque já saiu alguem do fifo!
            internalQueue.signalAll();
            pollclerk.setState(TPollClerk.PollClerkState.ID_CHECK);
        } finally {
            lock_CallingVoter.unlock();
        }
    }

    @Override
    public void closePS() {
        lock_changeState.lock();
        try{
            open = false;
        } finally {
            lock_changeState.unlock();
        }
    }

    @Override
    public boolean isOpen() {
        lock_isOpen.lock();
        try{
            return open;
        } finally{
            lock_isOpen.unlock();
        }
    }

    

    @Override
    public void exitingPS(TVoter voter) throws InterruptedException {
        // para deixar entrar uma pessoa
        lock_exitingPS.lock();
        try{
          voterInside--;
          externalQueue.signalAll();
          
          voter.setState(TVoter.VoterState.EXIT_PS);
        
        } finally {
            lock_exitingPS.unlock();
        }
    }
    
    @Override
    public boolean isEmpty(){
        lock_isEmpty.lock();
        try{
            return voterInside == 0;
        } finally {
            lock_isEmpty.unlock();
        }
    }

    @Override
    public boolean maxVotes(int maxVotes, IIDCheck idCheck) {
        lock_maxedVotes.lock();
        try{
            if(idCheck.getVoterRegisted() >= maxVotes) {
                return true;
            }
            return false;
        
        } finally{
            lock_maxedVotes.unlock();
        }
    }

}
