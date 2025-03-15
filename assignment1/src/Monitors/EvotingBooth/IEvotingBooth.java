package Monitors.EvotingBooth;

import Monitors.IAll;
import Threads.Voter.TVoter;

public interface IEvotingBooth extends IAll{
    void voting(TVoter t1);
    String getVotesResult();
}
