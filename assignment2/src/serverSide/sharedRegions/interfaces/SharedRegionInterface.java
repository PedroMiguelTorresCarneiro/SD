package serverSide.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

import serverSide.sharedRegions.SharedRegion;

public abstract class SharedRegionInterface {
    protected SharedRegion sharedRegion;

    public SharedRegionInterface(SharedRegion sharedRegion) {
        this.sharedRegion = sharedRegion;
    }

    public abstract Message processAndReply(Message inMessage) throws MessageException;
}
