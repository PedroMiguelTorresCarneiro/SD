package Threads.Voter;

import Monitors.PollStation.IPollStation;
import Monitors.IDCheck.IIDCheck;
import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.ExitPoll.IExitPoll;
import Monitors.Logs.ILogs;
import java.util.Random;

public class TVoter implements Runnable {
    private String id;
    private String vote;
    private static int voterIDs = 0;
    
    private final IPollStation pollStation;
    private final IIDCheck idCheck;
    private final IEvotingBooth votingBooth;
    private final IExitPoll exitPoll;
    private final ILogs log;
    private VoterState state;

    private TVoter(IPollStation pollStation, IIDCheck idCheck, IEvotingBooth votingBooth, IExitPoll exitPoll, ILogs log) {
        setId();
        setVote();
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.votingBooth = votingBooth;
        this.exitPoll = exitPoll;
        this.log = log;
        this.state = VoterState.WAITING;
    }
    
    public static Runnable getInstance(IPollStation pollStation, IIDCheck idCheck, IEvotingBooth votingBooth, IExitPoll exitPoll, ILogs log){
        return new TVoter(pollStation, idCheck, votingBooth, exitPoll, log);
    }
    
    public void setId(){
        this.id = "V" + Integer.toString(voterIDs++);
    }
    
    public void setVote(){
        Random random = new Random();
        this.vote = random.nextBoolean() ? "A" : "B";
    }
    
    public String getId(){
        return id;
    }
    
    public String getVote(){
        return vote;
    }
    
    
    @Override
    public void run() {
        while (state != VoterState.GO_HOME) {
            switch (state) {
                case WAITING -> {
                    log.log("[Voter] - " + id + " is waiting");
                    enterPollingStation();
                }
                case WAITING_FIFO -> waitForClerk();
                case CHECK_ID -> showID();
                case E_VOTING -> vote();
                case EXIT_POLL -> exitPoll();
            }
        }
        log.log("[Voter] - " + id + " terminou.");
    }

    private void enterPollingStation() {
        try {
            while ("Closed".equals(pollStation.getPollState()) || !pollStation.openFifo()) {
                log.log("[Voter] - " + id + " está à espera que a PollStation abra e tenha espaço.");
                while(true){
                    ;
                }
            }
            pollStation.enterPS(id);
            state = VoterState.WAITING_FIFO;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void waitForClerk() {
        log.log("[Voter] - " + id + " esperando chamada do Clerk");
        state = VoterState.CHECK_ID;
    }

    private void showID() {
        log.log("[Voter] - " + id + " is showing ID");
        if (idCheck.checkingID(id)) {
            log.log("[Clerk] - Validate ID and register");
            state = VoterState.E_VOTING;
        } else {
            log.log("[Clerk] - " + id + " tentou votar novamente e foi expulso.");
            state = VoterState.EXIT_POLL;
        }
    }

    private void vote() {
        log.log("[Voter] - " + id + " Voting on " + vote);
        votingBooth.voting(id, vote);
        state = VoterState.EXIT_POLL;
    }

    private void exitPoll() {
        log.log("[Voter] - " + id + " arrives to Exit Poll");
        exitPoll.addToExitPoll(id);

        if (exitPoll.isElectionOver()) {
            log.log("[Voter] - " + id + " morreu, eleições encerradas.");
            state = VoterState.GO_HOME;
        } else {
            reborn();
        }
    }

    public String getVote() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getVote() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getVote() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
