package Monitors.EvotingBooth;

import Monitors.IAll;
import java.util.Map;

public interface IEvotingBooth extends IAll{
    
    // Type-specific getInstance
    static IEvotingBooth getInstance() {
        return MEvotingBooth.getInstance();
    }
    
    String votar(String voterId) throws InterruptedException;
    int getTotalVotos();
    Map<String, String> gatherVotes() throws InterruptedException;
}
