package serverSide.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

import serverSide.sharedRegions.PollStation;

public  class PollStationInterface extends SharedRegionInterface {
    public PollStationInterface(PollStation pollStation) {
        super(pollStation);
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {

        PollStation pollStation = (PollStation) sharedRegion;
        return inMessage;
    }
}
