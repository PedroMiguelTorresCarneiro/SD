package Monitors.IDCheck;

import Threads.TVoter;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MIDCheck implements IIDCheck {
    private static MIDCheck instance;
    private final Set<String> idsChecked = new HashSet<>();
    private final ReentrantLock lock_idCheck, lock_getSize;
    private final Condition simulate_idCheck;
    private final Random random = new Random();

    private MIDCheck() {
        lock_idCheck = new ReentrantLock(true);
        simulate_idCheck = lock_idCheck.newCondition();
        
        lock_getSize = new ReentrantLock();
    }

    public static IIDCheck getInstance(){
        if (instance == null) {
            instance = new MIDCheck();
        }
        return instance;
    }

    @Override
    public boolean checkID(TVoter voter) throws InterruptedException{
        lock_idCheck.lock();
        try{
            voter.setState(TVoter.VoterState.CHECKING_ID);
            String voterId = voter.getID();
            
            // Simular a verificação de ID uma duração random entre 0,5s e 1,5s
            long randomDuration = 500 + random.nextInt(1001);
            simulate_idCheck.await(randomDuration, TimeUnit.MILLISECONDS);
            
            if(!idsChecked.contains(voterId)){
               idsChecked.add(voterId);
               return true;
            }
            return false;
        }finally{
           lock_idCheck.unlock();
        }
    }

    @Override
    public int getVoterRegisted() {
        lock_getSize.lock();
        try{
            return idsChecked.size();
        } finally {
            lock_getSize.unlock();
        }
    }
    
}
