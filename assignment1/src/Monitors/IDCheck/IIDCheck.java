package Monitors.IDCheck;

import Monitors.IAll;
import Threads.Voter.TVoter;

public interface IIDCheck extends IAll {
    void callAvoter();
    void RegiterID(TVoter t1);
    boolean checkingID(TVoter t1);
}
