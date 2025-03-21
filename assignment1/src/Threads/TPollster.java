package Threads;

import Monitors.ExitPoll.IExitPoll_TPollster;

public class TPollster implements Runnable {
    // Shared Regions
    private final IExitPoll_TPollster exitPoll;
    
    // Initiate State
    private static TPollster thread = null;
    private PollsterState state = PollsterState.WATING_VOTERS;

    public static enum PollsterState {
        WATING_VOTERS,
        SELECT_VOTER,
        PUBLISHING_RESULTS
    }
    
    private TPollster(IExitPoll_TPollster exitPoll) {
        this.exitPoll = exitPoll;
    }
    
    public static Runnable getInstance(IExitPoll_TPollster exitPoll){
        if (thread == null)
            thread = new TPollster(exitPoll);
        return thread;
    }

    @Override
    public void run() {
        try {
            while(state != PollsterState.PUBLISHING_RESULTS) {
                switch(state){
                    case PollsterState.WATING_VOTERS -> {
                        
                        if(!exitPoll.isOpen()){
                            setState(PollsterState.PUBLISHING_RESULTS);
                            break;
                        }
                        
                        exitPoll.conductSurvey(this);
                        
                    }
                    case PollsterState.SELECT_VOTER -> {
                        exitPoll.waitForVoters(this);
                    }
                    default -> {
                    }
                } 
            }
            exitPoll.publishResults(this);
            //System.out.println("⏹ TPollster has finished its work!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    public void setState(PollsterState state){
        this.state = state;
    }
        
}


