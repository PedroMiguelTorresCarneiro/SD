package Monitors.ExitPoll;

import Threads.TPollster;

interface IExitPoll_TPollster {
    boolean isOpen();
    void conductSurvey(TPollster pollster) throws InterruptedException;
    void waitForVoters(TPollster pollster);
    void publishResults(TPollster pollster) throws InterruptedException;
}
