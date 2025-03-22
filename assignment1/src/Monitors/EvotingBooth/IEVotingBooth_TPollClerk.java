package Monitors.EvotingBooth;

import Threads.TPollClerk;

/**
 * Interface IEVotingBooth_TPollClerk that defines the methods of the EVotingBooth with TPollClerk.
 */
public interface IEVotingBooth_TPollClerk {
    void gathering() throws InterruptedException;

    void publishElectionResults(TPollClerk pollClerk);

    int getSize();
}
