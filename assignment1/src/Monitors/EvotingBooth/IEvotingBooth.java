package Monitors.EvotingBooth;

import Monitors.IAll;
import Threads.TPollClerk;
import Threads.TVoter;

public interface IEvotingBooth extends IAll{
    
    // Type-specific getInstance
    static IAll getInstance() {
        return MEvotingBooth.getInstance();
    }

    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);

    void vote(TVoter voter) throws InterruptedException;

    String getVote(String voterId);

}
