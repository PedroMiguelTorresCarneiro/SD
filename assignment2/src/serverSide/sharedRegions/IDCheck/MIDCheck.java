package serverSide.sharedRegions.IDCheck;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import commInfra.interfaces.Repository.IRepo_IDChek;

/**
 * The MIDCheck class implements the ID check shared region.
 * The ID check shared region is used by the voters to check their ID.
 * 
 * @see IIDCheck_ALL
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class MIDCheck {
    /**
     * The instance atributte represents the singleton instance of this class.   
     */
    private static MIDCheck instance;

    /**
     * The log atributte represents the repository shared region.
     * Th ID Check shared region shares information to the repository,
     * to be logged (log file and on the terminal) or displayed on the GUI
     */
    private static IRepo_IDChek log;

    /**
     * The idsChecked atributte represents the set of IDs that have already been checked.
     */
    private final Set<String> idsChecked = new HashSet<>();

    /**
     * The lock_idCheck atributte represents the lock used to control the access this shared region.
     */
    private final ReentrantLock lock_idCheck;

    /**
     * The simulate_idCheck atributte represents the condition used to simulate the ID check.
     */
    private final Condition simulate_idCheck;

    /**
     * The random atributte represents the random number generator used to simulate the ID check.
     */
    private final Random random = new Random();
    
    
    /**
     * The MIDCheck constructor initializes the ID Check shared region and its atributtes.
     * 
     * @param logs The repository shared region
     */
    private MIDCheck(IRepo_IDChek logs) {
        log = logs;
        lock_idCheck = new ReentrantLock(true);
        simulate_idCheck = lock_idCheck.newCondition();
    }
    
    /**
     * The getInstance method returns the singleton instance of this class.
     * If the instance is null, a new instance is created.
     * 
     * @param logs The repository shared region
     * @return The singleton instance of this class
     */
    public static MIDCheck getInstance(IRepo_IDChek logs){
        if (instance == null) {
            instance = new MIDCheck(logs);
        }
        return instance;
    }
    
    /**
     * The checkID method simulates the voter checking his ID.
     * The voter waits for a random duration and then checks if his ID has already been checked.
     * If the ID has not been checked, the voter is accepted and the ID is added to the set of checked IDs.
     * This information is logged in the repository.
     * 
     * @param voterId The ID of the voter
     * @return boolean Returns true if the voter's ID is valid, false otherwise.
     * @throws java.lang.InterruptedException
     */
    public boolean checkID(String voterId) throws InterruptedException{
        lock_idCheck.lock();

        try{
            char accepted;
            
            long randomDuration = 500 + random.nextInt(1001);
            simulate_idCheck.await(randomDuration, TimeUnit.MILLISECONDS);
            
            if(!idsChecked.contains(voterId)){
                accepted = '✔';
                log.logIDCheck(voterId, accepted);

                idsChecked.add(voterId);
               return true;
            }else{
                accepted = '✖';
            }
            
            log.logIDCheck(voterId, accepted);
            
            return false;
        }finally{
           lock_idCheck.unlock();
        }
    }
    
    /**
     * Resets the singleton instance of the MIDCheck monitor.
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
