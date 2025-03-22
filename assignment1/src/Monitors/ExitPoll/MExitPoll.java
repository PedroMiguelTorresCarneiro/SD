package Monitors.ExitPoll;


import Threads.TPollster;
import Threads.TVoter;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Monitors.Repository.IRepo_ExitPoll;


public class MExitPoll implements IExitPoll_ALL {
    private static MExitPoll instance = null;
    private static IRepo_ExitPoll log;
    // Votantes que saíram da estação
    // Condition para entrevistas
    private final Random random = new Random();
    
    
    private final LinkedList<Character> votes = new LinkedList<>();
    private final ReentrantLock lock_checkingState, lock_publishResults, lockSurvey, lock_VoterAnswer, closeLock;
    private final Condition simulate_publishing, waitingForPollster, simulate_Voting;
    private boolean open = true;
    private long countA, countB;
    private final double lieRatio = 0.2;
    private final double beingChosen = 0.1;

    private MExitPoll(IRepo_ExitPoll logs) {
        log = logs;
        lock_checkingState = new ReentrantLock();
        
        lock_publishResults = new ReentrantLock();
        simulate_publishing = lock_publishResults.newCondition();
        closeLock = new ReentrantLock();
        

        
        lockSurvey = new ReentrantLock();
        waitingForPollster = lockSurvey.newCondition();
        
        lock_VoterAnswer = new ReentrantLock();
        simulate_Voting = lock_VoterAnswer.newCondition();
        
    }
    
    public static IExitPoll_ALL getInstance(IRepo_ExitPoll logs) {
        if (instance == null) {
            instance = new MExitPoll(logs);
        }
        return instance;
    }

    @Override
    public boolean isOpen() {
        lock_checkingState.lock();

        try{
            if (!open){  
               
                lockSurvey.lock();
                try{
                    waitingForPollster.signalAll();
                } finally {
                    lockSurvey.unlock();
                }
            }else{
                
            }

            return open;
        } finally{
            lock_checkingState.unlock();
        }
    }

    @Override
    public void conductSurvey(TPollster pollster) throws InterruptedException {
        lockSurvey.lock();

        try{
            waitingForPollster.signal();

            pollster.setState(TPollster.PollsterState.SELECT_VOTER);
        } finally {
            lockSurvey.unlock();
        }
        
    }

    @Override
    public void waitForVoters(TPollster pollster) {
        pollster.setState(TPollster.PollsterState.WAITING_VOTERS);
    }

    @Override
    public void publishResults(TPollster pollster) throws InterruptedException {
        lock_publishResults.lock();

        try{
            
            for (Character vote : votes){
                if (vote == 'A'){
                    countA++;
                }else{
                    countB++;
                }
            }
            
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulate_publishing.await(randomDuration, TimeUnit.MILLISECONDS);
            
            log.logSurveyResults(countA, countB, (countA > countB ? "A" : (countB > countA ? "B" : "TIE")));

            pollster.setState(TPollster.PollsterState.PUBLISHING_RESULTS);
            
        } finally {
            lock_publishResults.unlock();
        }
    }

    @Override
    public void close() {
        closeLock.lock();

        try{
            open = false;
        } finally{
            closeLock.unlock();
        }
    }

    @Override
    public boolean choosen() throws InterruptedException{
        lockSurvey.lock();

        try{
            waitingForPollster.await();
            
            return Math.random() < beingChosen;
            
        } finally {
            lockSurvey.unlock();
        }
    }

    @Override
    public void callForSurvey(Character vote, TVoter voter) throws InterruptedException{
        lock_VoterAnswer.lock();

        try{
                
            // Voter pode mentir ou não no voto!
            boolean lie = Math.random() < lieRatio;
            char lieOrNot = ' ';

            if (lie) {
                lieOrNot = 'L';
                votes.add(vote == 'B' ? 'A' : 'B');
            }
            
            
            //System.out.println("Voter " + voter.getID() + " is being interviewed");
            log.logSurvey(voter.getID(),lieOrNot);
            
            
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulate_Voting.await(randomDuration, TimeUnit.MILLISECONDS);
            
            voter.setState(TVoter.VoterState.ANSWER_SURVEY);
            
        } finally {
            lock_VoterAnswer.unlock();
        }
        
    }
}