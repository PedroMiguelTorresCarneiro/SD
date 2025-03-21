package Monitors.IDCheck;

import Threads.TVoter;

public interface IIDCheck_TVoter {
    boolean checkID(TVoter voter) throws InterruptedException;
}
