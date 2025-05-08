package clientSide.stubs;

import commInfra.interfaces.EvotingBooth.IEVotingBooth_ALL;;

public class STEvotingBooth extends Stub implements IEVotingBooth_ALL {
    // Constructor
    public STEvotingBooth(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
    }


    public void gathering() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gathering'");
    }

    public void publishElectionResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'publishElectionResults'");
    }

    public int getVotesCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVotesCount'");
    }

    
}
