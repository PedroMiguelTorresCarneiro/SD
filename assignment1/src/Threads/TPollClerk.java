package Threads;

import Monitors.EvotingBooth.IEVotingBooth_TPollClerk;
import Monitors.ExitPoll.IExitPoll_TPollClerk;
import Monitors.IDCheck.IIDCheck_TPollClerk;
import Monitors.PollStation.IPollStation_TPollClerk;


public class TPollClerk implements Runnable {
    // Shared Regions
    private final IPollStation_TPollClerk pollStation;
    private final IIDCheck_TPollClerk idCheck;
    private final IEVotingBooth_TPollClerk booth;
    private final IExitPoll_TPollClerk exitPoll;
    
    // Initiate State
    private static TPollClerk thread = null;
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
    
    public TPollClerk(IPollStation_TPollClerk pollStation, IIDCheck_TPollClerk idCheck, IEVotingBooth_TPollClerk booth, IExitPoll_TPollClerk exitPoll, int maxVotes) {
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.booth = booth;
        this.exitPoll = exitPoll;
        this.maxVotes = maxVotes;
    }
    
    public static Runnable getInstance(IPollStation_TPollClerk pollStation, IIDCheck_TPollClerk idCheck, IEVotingBooth_TPollClerk booth, IExitPoll_TPollClerk exitPoll, int maxVotes){
        if (thread == null)
            thread = new TPollClerk(pollStation, idCheck, booth, exitPoll, maxVotes);
        return thread;
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

                        //  System.out.println("PollClerk is checking the ID")
                        int maxVoters = idCheck.getVoterRegisted();
                        if(pollStation.maxVotes(maxVotes, maxVoters)){
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
