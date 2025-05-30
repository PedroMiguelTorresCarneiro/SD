package clientSide.stubs;

import commInfra.MessageType;
import commInfra.interfaces.EvotingBooth.IEVotingBooth_ALL;

public class STEvotingBooth extends Stub implements IEVotingBooth_ALL {
    private static STEvotingBooth instance = null;

    private STEvotingBooth(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
    }

    public static STEvotingBooth getInstance(String serverHostName, int serverPortNumb) {
        if (instance == null) {
            instance = new STEvotingBooth(serverHostName, serverPortNumb);
        }
        return instance;
    }

    @Override
    public void gathering() throws InterruptedException {
        sendMessage(MessageType.GATHERING_VOTES);
    }

    @Override
    public void publishElectionResults() {
        sendMessage(MessageType.PUBLISH_ELECTION_RESULTS);
    }

    @Override
    public int getVotesCount() {
        return intComm(MessageType.GET_VOTES_COUNT);
    }

    @Override
    public void vote(String voterId) throws InterruptedException {
        sendMessage(MessageType.VOTE, voterId);
    }

    @Override
    public char getVote(String voterId) {
        return charComm(MessageType.GET_VOTE, voterId);
    }
    
    @Override
    public void shutdown() {
        sendMessage(MessageType.SHUTDOWN);
    }

}
