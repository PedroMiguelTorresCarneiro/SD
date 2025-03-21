package Monitors.PollStation;

import Threads.TPollClerk;

public interface IPollStation_TPollClerk {
    void openPS(TPollClerk pollclerk) throws InterruptedException;
    void callNextVoter(TPollClerk pollclerk);
    void closePS();
    boolean isEmpty();
    public boolean maxVotes(int maxVotes, int maxVoters);
}
