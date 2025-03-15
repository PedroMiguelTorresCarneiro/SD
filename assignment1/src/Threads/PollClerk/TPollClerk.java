package Threads.PollClerk;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.PollStation.IPollStation;
import Monitors.ExitPoll.IExitPoll;

public class TPollClerk extends Thread {
    private final IPollStation pollStation;
    private final IExitPoll exitPoll;
    private final IEvotingBooth votingBooth;
    private final int maxVoters;
    private int voteCount = 0;
    private ClerkState state;

    private TPollClerk(IPollStation pollStation, IExitPoll exitPoll, IEvotingBooth votingBooth, int maxVoters) {
        this.pollStation = pollStation;
        this.exitPoll = exitPoll;
        this.votingBooth = votingBooth;
        this.maxVoters = maxVoters;
        this.state = ClerkState.OPEN_PS;
    }
    
    public Runnable getInstance(IPollStation pollStation, IExitPoll exitPoll, int maxVoters){
        return new TPollClerk(pollStation, exitPoll, votingBooth, maxVoters);
    }
    
    @Override
    public void run() {
        while (!votingBooth.finishConting()) {
            switch (state) {
                case OPEN_PS -> openPollStation();
                case WAIT_FOR_VOTERS -> waitForVoters();
                case CLOSE_PS -> closePollStation();
                case GATHER_VOTES -> gatherVotes();
            }
        }
    }

    private void openPollStation() {
        log.log("[PollClerk] - Open PollStation");
        pollStation.openStation();
        state = ClerkState.WAIT_FOR_VOTERS;
    }

    private void waitForVoters() {
        while (voteCount < maxVotes) {
            try {
                log.log("[PollClerk] - À espera de novos eleitores...");
                Thread.sleep(500);
                voteCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        state = ClerkState.CLOSE_PS;
    }

    private void closePollStation() {
        log.log("[PollClerk] - Closes PollStation");
        pollStation.closeStation();
        exitPoll.announceElectionEnd();
        state = ClerkState.GATHER_VOTES;
    }

    private void gatherVotes() {
        log.log("[Poll Clerk] - Gather Votes");
        log.log("[Poll Clerk] - Apuração finalizada.");
        state = ClerkState.GO_HOME;
    }
}
