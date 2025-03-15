package Threads.Pollster;

import Monitors.ExitPoll.IExitPoll;
import Monitors.PollStation.MPollStation;

public class TPollster extends Thread {
    private final IExitPoll exitPoll;
    private final MPollStation pollStation;
    private final PollsterState pollsterState;

    private TPollster(IExitPoll exitPoll,MPollStation pollStation) {
        this.pollsterState = PollsterState.WAIT;
        this.exitPoll = exitPoll;
        this.pollStation = pollStation;
    }
    
    public Runnable getInstance(IExitPoll exitPoll, MPollStation pollStation){
        return new TPollster(exitPoll, pollStation);
    }
    
    @Override
    public void run() {
        do{
            exitPoll.callVotersOnExitPoll();
            exitPoll.questionVote();
            exitPoll.receiveVote();
        }while(!pollStation.annoucesElectionEnd());
    }
}
