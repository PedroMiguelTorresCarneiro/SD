package Monitors.IDCheck;

import Monitors.IAll;
import Threads.TVoter;

public interface IIDCheck extends IAll {
    
    static IIDCheck getInstance() {
        return MIDCheck.getInstance();
    }

    boolean checkID(TVoter voter) throws InterruptedException;
    
    int getVoterRegisted();
}
