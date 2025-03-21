package Threads;
import Monitors.EvotingBooth.IEVotingBooth_TVoter;
import Monitors.ExitPoll.IExitPoll_TVoter;
import Monitors.IDCheck.IIDCheck_TVoter;
import Monitors.PollStation.IPollStation_TVoter;
import java.util.Random;

public class TVoter implements Runnable {
    // Shared Regions
    private final IPollStation_TVoter pollStation;
    private final IEVotingBooth_TVoter booth;
    private final IIDCheck_TVoter idCheck;
    private final IExitPoll_TVoter exitPoll;
    
    // Initiate State
    private VoterState state = VoterState.WATING_OUTSIDE;
    
    // Vars Initiation
    private String voterId;
    private final Random random = new Random();
    private boolean validID = true;
    private final double diffIdRatio = 0.6;
    private final double chooseToAnwser = 0.6;
    private final String original_ID;

    public static enum VoterState {
        WATING_OUTSIDE,
        WATING_INSIDE,
        ANWSER_SURVEY,
        CHECKING_ID,
        EXIT_PS,
        VOTING,
        GO_HOME
    }
    
    private TVoter(String voterId, IPollStation_TVoter pollStation,IIDCheck_TVoter idCheck, IEVotingBooth_TVoter booth, IExitPoll_TVoter exitPoll) {
        this.voterId = voterId;
        original_ID = voterId;
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.booth = booth;
        this.exitPoll = exitPoll;

        //System.out.println("Voter " + voterId + " is waiting outside");
    }
    
    // Static factory method
    public static Runnable getInstance(String voterId, IPollStation_TVoter pollStation,IIDCheck_TVoter idCheck, IEVotingBooth_TVoter booth, IExitPoll_TVoter exitPoll) {
        return new TVoter(voterId, pollStation, idCheck, booth, exitPoll);
    }
    

    @Override
    public void run() {
        try {
            while(state != VoterState.GO_HOME){
                switch(state){
                    case VoterState.WATING_OUTSIDE -> {
                        if(pollStation.isCLosedAfterElection()){
                            //System.out.println("Voter " + voterId + " is going home");

                            setState(VoterState.GO_HOME);
                            
                            break;
                        }

                        pollStation.enterPS(this);
                    }
                    case VoterState.WATING_INSIDE -> {
                        validID = idCheck.checkID(this);
                    }
                    case VoterState.CHECKING_ID -> {
                        //System.out.println("Voter " + voterId + " ID is being checked");

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
                        //System.out.println("Voter " + original_ID + " is exiting the poll station");

                        if(!exitPoll.choosen()){
                            reborn();
                            break;
                        }
                        
                        if(Math.random() < chooseToAnwser){
                            exitPoll.callForSurvey(booth.getVote(voterId), this);
                        }else{
                            reborn();
                            break;
                        }
                        
                        
                    }
                    case VoterState.ANWSER_SURVEY -> {
                        //System.out.println("Voter " + voterId + " is answering the survey");
                        reborn();
                    }
                    default -> {}
                }
                
            }
            System.out.println("⏹ TVoter"+ original_ID +"has finished its work!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    public void setState(VoterState state) {
        this.state = state;
    }
    
    private void reborn() {
        boolean DiffID = Math.random() < diffIdRatio;

        if (DiffID) {
            generateNewID();
            //System.out.println("Voter " + voterId + " reborned(new ID)");
        }

        setState(VoterState.WATING_OUTSIDE);
        //System.out.println("Voter " + voterId + " is reborned");
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
