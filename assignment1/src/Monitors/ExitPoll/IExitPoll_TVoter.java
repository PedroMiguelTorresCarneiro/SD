package Monitors.ExitPoll;

import Threads.TVoter;

public interface IExitPoll_TVoter {
    boolean choosen() throws InterruptedException;
    void callForSurvey(Character vote, TVoter voter) throws InterruptedException;
}
