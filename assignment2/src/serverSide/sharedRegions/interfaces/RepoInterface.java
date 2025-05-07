package serverSide.sharedRegions.interfaces;

import commInfra.Message;
import commInfra.MessageException;

import serverSide.sharedRegions.Repo;

public class RepoInterface extends SharedRegionInterface {
    public RepoInterface(Repo repo) {
        super(repo);
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Repo repo = (Repo) sharedRegion;
        return inMessage;
    }   
}
