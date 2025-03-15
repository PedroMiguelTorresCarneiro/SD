package Monitors.ExitPoll;

import Monitors.IAll;
import Threads.Voter.TVoter;

public interface IExitPoll extends IAll{
    public void callVoterExitPoll();
    public void receiveVoterAnswer(TVoter t1);
    public void questionsForVotes();
    public void reborn(TVoter t1);
}
