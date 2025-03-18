package Monitors.ExitPoll;

import Monitors.IAll;

public interface IExitPoll extends IAll{
    
    static IExitPoll getInstance() {
        return MExitPoll.getInstance();
    }

    public boolean open();

    public void announceResults();

    public void conductSurvey();

    public void waitForVoters();

    public void publishResults();

    public void close();

    public boolean choosen();

    public void callForSurvey(String voterId, String vote);
}
