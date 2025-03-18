package Threads;

import Monitors.ExitPoll.IExitPoll;

public class TPollster extends Thread {
    // Shared Regions
    private final IExitPoll exitPoll;
    
    // Initiate State
    private PollsterState state = PollsterState.WATING_VOTERS;

    private static enum PollsterState {
        WATING_VOTERS,
        SELECT_VOTER,
        PUBLISHING_RESULTS
    }
    
    public TPollster(IExitPoll exitPoll) {
        this.exitPoll = exitPoll;
    }

    @Override
    public void run() {
        try {
            while(state != PollsterState.PUBLISHING_RESULTS) {
                switch(state){
                    case PollsterState.WATING_VOTERS -> {
                        
                        if(!exitPoll.open()){
                            exitPoll.announceResults();
                            break;
                        }
                        
                        exitPoll.conductSurvey();
                        
                    }
                    case PollsterState.SELECT_VOTER -> {
                        exitPoll.waitForVoters();
                    }
                    default -> {
                    }
                } 
            }
            exitPoll.publishResults();
            System.out.println("⏹ TPollster terminou o seu trabalho!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    public void setState(PollsterState state){
        this.state = state;
    }
        
}


