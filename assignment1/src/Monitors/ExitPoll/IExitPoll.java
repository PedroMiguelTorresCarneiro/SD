package Monitors.ExitPoll;

import Monitors.IAll;
import Threads.TPollster;
import Threads.TVoter;
import Monitors.Repository.IRepo;

public interface IExitPoll extends IAll{
    
    static IExitPoll getInstance(IRepo logs) {
        return MExitPoll.getInstance(logs);
    }

    boolean isOpen();

    void publishResults(TPollster pollster) throws InterruptedException;

    void conductSurvey(TPollster pollster) throws InterruptedException;

    void waitForVoters(TPollster pollster);

    void close();

    boolean choosen() throws InterruptedException;

    void callForSurvey(Character vote, TVoter voter) throws InterruptedException;
}
