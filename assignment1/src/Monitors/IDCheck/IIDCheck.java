package Monitors.IDCheck;

import Monitors.IAll;
import Threads.TVoter;
import Monitors.Repository.IRepo;

public interface IIDCheck extends IAll {
    
    static IIDCheck getInstance(IRepo logs) {
        return MIDCheck.getInstance(logs);
    }

    boolean checkID(TVoter voter) throws InterruptedException;
    
    int getVoterRegisted();

    
}
