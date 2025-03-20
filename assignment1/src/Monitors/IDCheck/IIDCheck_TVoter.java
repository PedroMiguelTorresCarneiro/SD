package Monitors.IDCheck;

import Threads.TVoter;

interface IIDCheck_TVoter {
    boolean checkID(TVoter voter) throws InterruptedException;
}
