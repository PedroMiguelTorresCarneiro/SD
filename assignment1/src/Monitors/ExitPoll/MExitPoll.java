package Monitors.ExitPoll;


import Threads.TPollster;
import Threads.TVoter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MExitPoll implements IExitPoll {
    private static MExitPoll instance = null;
    // Armazena o voto real do votante
    // Votantes que saíram da estação
    // Condition para entrevistas
    private final Random random = new Random();
    
    
    private final LinkedList<String> votes = new LinkedList<>();
    private final ReentrantLock lock_checkingState, lock_publishResults, lock_survey, lock_waitingSurvey, lock_VoterAnswer, lock_ConductSurvey;
    private final Condition simulate_publishing, simulate_survey, waitingForPollster, simulate_Voting;
    private boolean open = true;
    private long countA, countB;
    private final double choosingRatio = 0.7;
    
    private MExitPoll() {
        lock_checkingState = new ReentrantLock();
        
        lock_publishResults = new ReentrantLock();
        simulate_publishing = lock_publishResults.newCondition();
        
        lock_survey = new ReentrantLock();
        simulate_survey = lock_survey.newCondition();
        
        lock_waitingSurvey = new ReentrantLock();
        waitingForPollster = lock_waitingSurvey.newCondition();
        
        lock_VoterAnswer = new ReentrantLock();
        simulate_Voting = lock_VoterAnswer.newCondition();
        
        lock_ConductSurvey = new ReentrantLock();
    }
    
    public static IExitPoll getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }

    @Override
    public boolean isOpen() {
        lock_checkingState.lock();
        try{
            return open;
        } finally{
            lock_checkingState.unlock();
        }
    }

    @Override
    public void conductSurvey(TPollster pollster) throws InterruptedException {
        lock_ConductSurvey.lock();
        try{
            simulate_Voting.signalAll();
            pollster.setState(TPollster.PollsterState.SELECT_VOTER);
        } finally {
            lock_ConductSurvey.unlock();
        }
        
    }

    @Override
    public void waitForVoters(TPollster pollster) {
        pollster.setState(TPollster.PollsterState.WATING_VOTERS);
    }

    @Override
    public void publishResults(TPollster pollster) throws InterruptedException {
        lock_publishResults.lock();
        try{
            
            for (String vote : votes){
                if (vote.equals("A")){
                    countA++;
                }else{
                    countB++;
                }
            }
            
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulate_publishing.await(randomDuration, TimeUnit.MILLISECONDS);
            
            System.out.println("Total de votos em A: " + countA);
            System.out.println("Total de votos em B: " + countB);
            System.out.println("\n\n *VENCEDOR* -> "+ (countA > countB ? "A" : (countB > countA ? "B" : "EMPATE")) );
            
            pollster.setState(TPollster.PollsterState.PUBLISHING_RESULTS);
            
        } finally {
            lock_publishResults.unlock();
        }
    }

    @Override
    public void close() {
        lock_checkingState.lock();
        try{
            open = false;
        } finally{
            lock_checkingState.unlock();
        }
    }

    @Override
    public boolean choosen() throws InterruptedException{
        lock_waitingSurvey.lock();
        try{
            waitingForPollster.await();
            
            return Math.random() > choosingRatio;
            
        } finally {
            lock_waitingSurvey.unlock();
        }
    }

    @Override
    public void callForSurvey(String vote, TVoter voter) throws InterruptedException{
        lock_VoterAnswer.lock();
        try{
                
            // Voter pode mentir ou não no voto!
            boolean lie = Math.random() < choosingRatio;
            if (lie) {
                votes.add(vote.equals("B") ? "A" : "B");
            }
        
            // Simular o voto uma duração random entre 0,5s e 2s
            long randomDuration = 500 + random.nextInt(1501);
            simulate_Voting.await(randomDuration, TimeUnit.MILLISECONDS);
            
            voter.setState(TVoter.VoterState.ANWSER_SURVEY);
            
        } finally {
            lock_VoterAnswer.unlock();
        }
        
    }
    
    
}