package Threads;
import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.IDCheck.IIDCheck;
import Monitors.PollStation.IPollStation;
import java.util.Random;

public class TVoter extends Thread {
    // Shared Regions
    private final IPollStation pollStation;
    private final IEvotingBooth booth;
    private final IIDCheck idCheck;
    private final IExitPoll exitPoll;
    
    // Initiate State
    private VoterState state = VoterState.WATING_OUTSIDE;
    
    // Vars Initiation
    private String voterId;
    private final Random random = new Random();
    private boolean validID = true;
    private final double diffIdRatio = 0.6;

    public static enum VoterState {
        WATING_OUTSIDE,
        WATING_INSIDE,
        ANWSER_SURVEY,
        CHECKING_ID,
        EXIT_PS,
        VOTING,
        GO_HOME
    }
    
    public TVoter(String voterId, IPollStation pollStation,IIDCheck idCheck, IEvotingBooth booth, IExitPoll exitPoll) {
        this.voterId = voterId;
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.booth = booth;
        this.exitPoll = exitPoll;

        System.out.println("Voter " + voterId + " is waiting outside");
    }

    @Override
    public void run() {
        try {
            while(state != VoterState.GO_HOME){
                switch(state){
                    case VoterState.WATING_OUTSIDE -> {
                        if(pollStation.isCLosedAfterElection()){
                            System.out.println("Voter " + voterId + " is going home");

                            setState(VoterState.GO_HOME);

                            break;
                        }

                        pollStation.enterPS(this);
                    }
                    case VoterState.WATING_INSIDE -> {
                        validID = idCheck.checkID(this);
                    }
                    case VoterState.CHECKING_ID -> {
                        System.out.println("Voter " + voterId + " ID is being checked");

                        if(!validID){
                            pollStation.exitingPS(this);
                            break;
                        }

                        booth.vote(this);
                    }
                    case VoterState.VOTING -> {
                        pollStation.exitingPS(this);
                    }
                    case VoterState.EXIT_PS ->{
                        System.out.println("Voter " + voterId + " is exiting the poll station");

                        if(!exitPoll.choosen()){
                            reborn();
                            break;
                        }

                        exitPoll.callForSurvey(booth.getVote(voterId), this);
                    }
                    case VoterState.ANWSER_SURVEY -> {
                        System.out.println("Voter " + voterId + " is answering the survey");
                        reborn();
                    }
                    default -> {}
                }
                
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    public void setState(VoterState state) {
        this.state = state;
    }
    
    private void reborn() {
        boolean DiffID = Math.random() > diffIdRatio;

        if (DiffID) {
            generateNewID();
            System.out.println("Voter " + voterId + " reborned(new ID)");
        }

        setState(VoterState.WATING_OUTSIDE);
        System.out.println("Voter " + voterId + " is reborned");
    }
    
    private void generateNewID(){
        String newId;
        do{
          newId = "V" + random.nextInt(0, 64);
        }while(voterId.equals(newId));
        
        voterId = newId;
    }
    
    public String getID(){
        return voterId;
    }
}
