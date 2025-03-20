package Monitors.EvotingBooth;

import Monitors.Logs.ILogs;
import Threads.TPollClerk;
import Threads.TVoter;

public interface IEvotingBooth{
    
    // Type-specific getInstance
    static IEvotingBooth getInstance(ILogs logs) {
        return MEvotingBooth.getInstance(logs);
    }

    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);

    void vote(TVoter voter) throws InterruptedException;

    Character getVote(String voterId);

}
