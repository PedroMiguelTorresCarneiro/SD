package serverSide.main.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

public class EvotingBoothInterface extends SharedRegionInterface {
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        return inMessage;
    }
    
}
