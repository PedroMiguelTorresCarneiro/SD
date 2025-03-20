package Threads;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.IDCheck.IIDCheck;
import Monitors.PollStation.IPollStation;


public class TPollClerk extends Thread {
    // Shared Regions
    private final IPollStation pollStation;
    private final IIDCheck idCheck;
    private final IEvotingBooth booth;
    private final IExitPoll exitPoll;
    
    // Initiate State
    private PollClerkState state = PollClerkState.OPEN_PS;
    
    // VARS Initiation
    private final int maxVotes; // Número máximo de votos para encerrar

    public static enum PollClerkState {
        ID_CHECK_WAIT,
        CLOSING_PS,
        OPEN_PS,
        ID_CHECK,
        INFORMING_EP,
        GATHERING_VOTES,
        PUBLISHING_WINNER
    }
    
    public TPollClerk(IPollStation pollStation, IIDCheck idCheck, IEvotingBooth booth, IExitPoll exitPoll, int maxVotes) {
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.booth = booth;
        this.exitPoll = exitPoll;
        this.maxVotes = maxVotes;
    }
    
    public static Runnable getInstace(IPollStation pollStation, IIDCheck idCheck, IEvotingBooth booth, IExitPoll exitPoll, int maxVotes){
        return new TPollClerk(pollStation, idCheck, booth, exitPoll, maxVotes);
    }

    @Override
    public void run() {
        try {
            while (state != PollClerkState.PUBLISHING_WINNER) {
                switch(state){
                    case PollClerkState.OPEN_PS ->{
                        // Open PollStation

                      //  System.out.println("PollClerk is opening the PollStation");
                        pollStation.openPS(this);
                    }
                    case PollClerkState.ID_CHECK_WAIT ->{
                     //   System.out.println("PollClerk is waiting for the next voter");
                        pollStation.callNextVoter(this);
                    }
                    case PollClerkState.ID_CHECK -> {
                        // Control para fechar a PollStation

                      //  System.out.println("PollClerk is checking the ID");
             
                        if(pollStation.maxVotes(maxVotes, idCheck)){
                            pollStation.closePS();
                        }
                        
                        // flag para dizer quadno já não ha ninguem na PollStation
                        if(pollStation.isEmpty()){
                            setState(PollClerkState.INFORMING_EP);
                            break;
                        }

                        setState(PollClerkState.ID_CHECK_WAIT);
                  
                    }
                    case PollClerkState.INFORMING_EP -> {
                        exitPoll.close();

                        booth.gathering();

                        setState(PollClerkState.GATHERING_VOTES);
                    }
                    case PollClerkState.GATHERING_VOTES -> {

                      //  System.out.println("PollClerk is gathering the votes");
                        booth.publishElectionResults(this);
                        setState(PollClerkState.PUBLISHING_WINNER);
                    }
                    default -> {}
                }
            }

            //System.out.println("⏹ TPollClerk terminou o seu trabalho!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setState(PollClerkState state){
        this.state = state;
    }


    public PollClerkState getClerkState(){
        return state;
    }
    
    
}
