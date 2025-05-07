package serverSide.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;
import serverSide.sharedRegions.ExitPoll;

public class ExitPollInterFace extends SharedRegionInterface {
     public ExitPollInterFace(ExitPoll exitPoll) {
        super(exitPoll);
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        ExitPoll exitPoll = (ExitPoll) sharedRegion;


        return inMessage;
    }  
} 