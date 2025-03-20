package Monitors.PollStation;

import Monitors.IDCheck.IIDCheck;
import Threads.TPollClerk;
import Threads.TVoter;
import Monitors.Repository.IRepo;

public interface IPollStation {
    
    static IPollStation getInstance(int capacidadeMax, IRepo logs){
        return MPollStation.getInstance(capacidadeMax, logs);
    }

    void openPS(TPollClerk pollclerk) throws InterruptedException;

    void callNextVoter(TPollClerk pollclerk);

    void closePS();

    boolean isCLosedAfterElection();

    void enterPS(TVoter voter) throws InterruptedException;

    void exitingPS(TVoter voter) throws InterruptedException;
    
    boolean isEmpty();
    
    public boolean maxVotes(int maxVotes, int maxVoters);
    
}
