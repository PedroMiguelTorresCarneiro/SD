package serverSide.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

import serverSide.sharedRegions.EvotingBooth;

public class EvotingBoothInterface extends SharedRegionInterface {
    public EvotingBoothInterface( EvotingBooth evotingBooth) {
        super(evotingBooth);
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        EvotingBooth evotingBooth = (EvotingBooth) sharedRegion;


        return inMessage;
    }
    
}
