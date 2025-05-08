package serverSide.sharedRegions.ExitPoll;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import commInfra.interfaces.Repository.IRepo_ExitPoll;

/**
 * The MExitPoll class implements the IExitPoll_ALL interface and represents the Exit Poll shared region.
 * The exit poll shared region is responsible for simulating the surveys lead by the pollster, and the voters' answers.
 * and also for the clerk publishing the results of the survey.   
 * 
 * @see IExitPoll_ALL
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class MExitPoll {

    /**
     * The instance atributte represents the singleton instance of this class.
     */
    private static MExitPoll instance = null;

    /**
     * The log atributte represents the repository shared region.
     * The Exit Poll shared region shares information to the repository,
     * to be logged (log file and on the terminal) or displayed on the GUI
     */
    private static IRepo_ExitPoll log;
    
    /**
     * The random atributte represents the random number generator used to simulate the survey.
     */
    private final Random random = new Random();
    
    /**
     * The votes atributte represents the list of votes that have already been conducted.
     */
    private final LinkedList<Character> votes = new LinkedList<>();

    /**
     * The lockCheckingState, lockPublishResults, lockSurvey and lockVoterAnswer atributtes represent the locks used to control the access this shared region.  
     * Their lock funcionalities are descbed in their respective names
     */
    private final ReentrantLock lockCheckingState, lockPublishResults, lockSurvey, lockVoterAnswer, closeLock;

    /**
     * The simulatePublishing, waitingForPollster and simulateVoting atributtes represent the locks conditions of this class.
     * Their condition funcionalities are descbed in their respective names
     */
    private final Condition simulatePublishing, waitingForPollster, simulateVoting;

    /**
     * The open atributte represents a flag that indicates if the exit poll is open or closed.
     */
    private boolean open = true;

    /**
     * The countA and countB atributtes represent the number of votes for each candidate.
     */
    private long countA, countB;
    
    /**
     * The LIE_PROB constant atribute represents the probability of a voter lying in the survey.
     */
    private static final double LIE_PROB = 0.2;

    /**
     * The BEING_CHOSEN_PROB constant atribute represents the probability of a voter being chosen to answer the survey.
     */
    private static final double BEING_CHOSEN_PROB = 0.50;
    
    /**
     * The MExitPoll constructor initializes the Exit Poll shared region and its atributtes.
     * 
     * @param logs The repository shared region
     */
    private MExitPoll(IRepo_ExitPoll logs) {
        log = logs;
        lockCheckingState = new ReentrantLock();
        
        lockPublishResults = new ReentrantLock();
        simulatePublishing = lockPublishResults.newCondition();
        closeLock = new ReentrantLock();
        
        lockSurvey = new ReentrantLock();
        waitingForPollster = lockSurvey.newCondition();
        
        lockVoterAnswer = new ReentrantLock();
        simulateVoting = lockVoterAnswer.newCondition();     
    }
    
    /**
     * The getInstance method returns the singleton instance of this class.
     * If the instance is null, a new instance is created.
     * @param logs The repository shared region
     * @return The singleton instance of this class
     */
    public static MExitPoll getInstance(IRepo_ExitPoll logs) {
        if (instance == null) {
            instance = new MExitPoll(logs);
        }
        return instance;
    }
     
    /**
     * The isOpen method returns the flag that indicates if the exit poll is open or closed.
     * If the exit poll is closed, the method notifies the pollster that the exit poll is closed.
     * 
     * @return The flag that indicates if the exit poll is open or closed
     */
    public boolean isOpen() {
        lockCheckingState.lock();

        try{
            if (!open){  
                /* Notifies the voters which are waiting for the pollster that
                   the exit poll is closed 
                */
                lockSurvey.lock();

                try{
                    waitingForPollster.signalAll();
                } finally {
                    lockSurvey.unlock();
                }
            }

            return open;
        } finally{
            lockCheckingState.unlock();
        }
    }
   
    /**
     * The conductSurvey method is called by the pollster to conduct a survey.
     * The voter is notified that the pollster is waiting for him.
     * @throws java.lang.InterruptedException
     */
    public void conductSurvey() throws InterruptedException {
        lockSurvey.lock();

        try{
            waitingForPollster.signal();
        } finally {
            lockSurvey.unlock();
        }
        
    }
    
    /**
     * The publishResults method is called by the pollster to publish the results of the survey.The pollster is set to the PUBLISHING_RESULTS state.
     * The results of the survey are logged (in the log file and on the terminal) and displayed on the GUI.
     * 
     * @throws java.lang.InterruptedException
     */
    public void publishResults() throws InterruptedException {
        lockPublishResults.lock();

        try{   
            for (Character vote : votes){
                if (vote == 'A'){
                    countA++;
                }else{
                    countB++;
                }
            }
            
            // Simulate the vote with a random duration between 0.5s and 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulatePublishing.await(randomDuration, TimeUnit.MILLISECONDS);
            
            log.logSurveyResults(countA, countB, (countA > countB ? "A" : (countB > countA ? "B" : "TIE")));

            //pollster.setState(TPollster.PollsterState.PUBLISHING_RESULTS);
            
        } finally {
            lockPublishResults.unlock();
        }
    }
    
    /**
     * The close method is called by the pollster to close the exit poll.
     * The exit poll is set to closed.
     */
    public void close() {
        closeLock.lock();

        try{
            open = false;
        } finally{
            closeLock.unlock();
        }
    }
     
    /**
     * The choosen method is called by the voter to check if he was chosen to answer the survey.
     * The voter waits for the pollster to notify him that he was chosen.
     * 
     * @return True if the voter was chosen, false otherwise
     * @throws java.lang.InterruptedException
     */
    public boolean choosen() throws InterruptedException{
        lockSurvey.lock();

        try{
            waitingForPollster.await();
            
            return Math.random() < BEING_CHOSEN_PROB;
            
        } finally {
            lockSurvey.unlock();
        }
    }
   
    /**
     * The callForSurvey method is called by the voter to answer the survey.The voter may chose to lie in the survey with a probability of LIE_PROB.
     * The voter answers are logged (in the log file and on the terminal) and displayed on the GUI.
     * 
     * @param vote The vote of the voter
     * @param voterId The ID of the voter
     * @throws java.lang.InterruptedException
     */
    public void callForSurvey(char vote, String voterId) throws InterruptedException{
        lockVoterAnswer.lock();

        try{
            boolean lie = Math.random() < LIE_PROB;
            char lieOrNot = ' ';

            if (lie) {
                lieOrNot = 'L';
                votes.add(vote == 'B' ? 'A' : 'B');
            }
             
            log.logSurvey(voterId,lieOrNot);
            
            // Simulate the survey response with a random duration between 0.5s and 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulateVoting.await(randomDuration, TimeUnit.MILLISECONDS);
            
            //voter.setState(TVoter.VoterState.ANSWER_SURVEY);
            
        } finally {
            lockVoterAnswer.unlock();
        }
        
    }
    
    /**
    * Resets the singleton instance of the MExitPoll monitor.
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