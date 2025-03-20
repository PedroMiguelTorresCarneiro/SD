package Monitors.EvotingBooth;

import Threads.TPollClerk;
import Threads.TVoter;
import Monitors.Repository.IRepo;

public interface IEvotingBooth{
    
    // Type-specific getInstance
    static IEvotingBooth getInstance(IRepo logs) {
        return MEvotingBooth.getInstance(logs);
    }

    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);

    void vote(TVoter voter) throws InterruptedException;

    Character getVote(String voterId);

}
