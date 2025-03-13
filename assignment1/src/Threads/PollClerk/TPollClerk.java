package Threads.PollClerk;

import Monitors.PollStation.IPollStation;
import Monitors.Logs.ILogs;
import Monitors.ExitPoll.IExitPoll;

public class TPollClerk implements Runnable {
    private final IPollStation pollStation;
    private final IExitPoll exitPoll;
    private final ILogs log;
    private final int maxVotes;
    private int voteCount = 0;
    private ClerkState state;

    public TPollClerk(IPollStation pollStation, IExitPoll exitPoll, ILogs log, int maxVotes) {
        this.pollStation = pollStation;
        this.exitPoll = exitPoll;
        this.log = log;
        this.maxVotes = maxVotes;
        this.state = ClerkState.OPEN_PS;
    }

    @Override
    public void run() {
        while (state != ClerkState.GO_HOME) {
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
