package serverSide.main.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

public abstract class SharedRegionInterface {

    public abstract Message processAndReply(Message inMessage) throws MessageException;
}
