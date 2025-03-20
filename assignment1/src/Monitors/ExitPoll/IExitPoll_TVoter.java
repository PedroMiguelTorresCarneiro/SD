package Monitors.ExitPoll;

import Threads.TVoter;

interface IExitPoll_TVoter {
    boolean choosen() throws InterruptedException;
    void callForSurvey(Character vote, TVoter voter) throws InterruptedException;
}
