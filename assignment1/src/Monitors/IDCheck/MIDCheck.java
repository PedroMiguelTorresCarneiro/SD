package Monitors.IDCheck;

import Threads.TVoter;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Monitors.Repository.IRepo_IDChek;

public class MIDCheck implements IIDCheck_ALL {
    private static MIDCheck instance;
    private static IRepo_IDChek log;
    private final Set<String> idsChecked = new HashSet<>();
    private final ReentrantLock lock_idCheck;
    private final Condition simulate_idCheck;
    private final Random random = new Random();

    private MIDCheck(IRepo_IDChek logs) {
        log = logs;
        lock_idCheck = new ReentrantLock(true);
        simulate_idCheck = lock_idCheck.newCondition();
    }

    public static IIDCheck_ALL getInstance(IRepo_IDChek logs){
        if (instance == null) {
            instance = new MIDCheck(logs);
        }
        return instance;
    }

    @Override
    public boolean checkID(TVoter voter) throws InterruptedException{
        lock_idCheck.lock();

        try{
            voter.setState(TVoter.VoterState.CHECKING_ID);
            String voterId = voter.getID();
            char accepted;
            //log.logIDCheck(voter.getID());
            
            // Simular a verificação de ID uma duração random entre 0,5s e 1,5s
            long randomDuration = 500 + random.nextInt(1001);
            simulate_idCheck.await(randomDuration, TimeUnit.MILLISECONDS);
            
            if(!idsChecked.contains(voterId)){
                accepted = '✔';
                log.logIDCheck(voter.getID(), accepted);
                idsChecked.add(voterId);
               return true;
            }else{
                accepted = '✖';
            }
            
            log.logIDCheck(voter.getID(), accepted);
            
            return false;
        }finally{
           lock_idCheck.unlock();
        }
    }
}
