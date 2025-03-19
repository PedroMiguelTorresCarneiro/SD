package Monitors.PollStation;

import Monitors.IDCheck.IIDCheck;
import Threads.TPollClerk;
import Threads.TVoter;

public interface IPollStation {
    
    static IPollStation getInstance(int capacidadeMax){
        return MPollStation.getInstance(capacidadeMax);
    }

    void openPS(TPollClerk pollclerk) throws InterruptedException;

    void callNextVoter(TPollClerk pollclerk);

    void closePS();

    boolean isCLosedAfterElection();

    void enterPS(TVoter voter) throws InterruptedException;

    void exitingPS(TVoter voter) throws InterruptedException;
    
    boolean isEmpty();
    
    public boolean maxVotes(int maxVotes, IIDCheck idCheck);
    
}
