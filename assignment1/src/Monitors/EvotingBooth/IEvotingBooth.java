package Monitors.EvotingBooth;

import Threads.TPollClerk;
import Threads.TVoter;

public interface IEvotingBooth{
    
    // Type-specific getInstance
    static IEvotingBooth getInstance() {
        return MEvotingBooth.getInstance();
    }

    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);

    void vote(TVoter voter) throws InterruptedException;

    Character getVote(String voterId);

}
