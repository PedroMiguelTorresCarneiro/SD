package Monitors.EvotingBooth;

import Threads.TVoter;

/**
 * Interface IEVotingBooth_TVoter that defines the methods of the class EVotingBooth_TVoter.
 */
public interface IEVotingBooth_TVoter{
    void vote(TVoter voter) throws InterruptedException;
    Character getVote(String voterId);
}
