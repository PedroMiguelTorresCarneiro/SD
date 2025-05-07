package serverSide.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

import serverSide.sharedRegions.IDCheck;

public class IDCheckInterface extends SharedRegionInterface {
    public IDCheckInterface(IDCheck idCheck) {
        super(idCheck);
    }


    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        IDCheck idCheck = (IDCheck) sharedRegion;
        return inMessage;
    }
}
