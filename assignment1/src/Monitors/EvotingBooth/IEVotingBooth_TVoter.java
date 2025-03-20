package Monitors.EvotingBooth;

import Threads.TVoter;

public interface IEVotingBooth_TVoter{
    void vote(TVoter voter) throws InterruptedException;
    Character getVote(String voterId);
}
