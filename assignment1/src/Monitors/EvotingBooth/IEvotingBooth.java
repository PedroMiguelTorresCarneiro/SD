package Monitors.EvotingBooth;

import Monitors.IAll;
import Threads.TPollClerk;

public interface IEvotingBooth extends IAll{
    
    // Type-specific getInstance
    static IAll getInstance() {
        return MEvotingBooth.getInstance();
    }

    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);

    void vote();

    String getVote(String voterId);

}
