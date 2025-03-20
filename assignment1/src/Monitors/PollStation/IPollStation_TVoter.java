package Monitors.PollStation;

import Threads.TVoter;

interface IPollStation_TVoter {
    boolean isCLosedAfterElection();
    void enterPS(TVoter voter) throws InterruptedException;
    void exitingPS(TVoter voter) throws InterruptedException;
}
