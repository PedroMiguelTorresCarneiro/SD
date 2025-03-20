package Monitors.IDCheck;

import Monitors.IAll;
import Monitors.Logs.ILogs;
import Threads.TVoter;

public interface IIDCheck extends IAll {
    
    static IIDCheck getInstance(ILogs logs) {
        return MIDCheck.getInstance(logs);
    }

    boolean checkID(TVoter voter) throws InterruptedException;
    
    int getVoterRegisted();
}
