package Monitors.EvotingBooth;

import Threads.TPollClerk;

public interface IEVotingBooth_TPollClerk {
    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);
}
