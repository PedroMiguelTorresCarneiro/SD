package clientSide.stubs;

import commInfra.MessageType;
import commInfra.interfaces.PollStation.IPollStation_ALL;

public class STPollStation extends Stub implements IPollStation_ALL {
    private static STPollStation instance = null;

    private STPollStation(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
    }

    public static STPollStation getInstance(String serverHostName, int serverPortNumb) {
        if (instance == null) {
            instance = new STPollStation(serverHostName, serverPortNumb);
        }
        return instance;
    }

    @Override
    public boolean isCLosedAfterElection() {
        return boolComm(MessageType.PS_IS_CLOSED_AFTER_ELECTION);
    }

    @Override
    public boolean canEnterPS(String voterId) throws InterruptedException {
        return boolComm(MessageType.CAN_ENTER_PS, voterId);
    }

    @Override
    public void exitingPS(String voterId) throws InterruptedException {
        sendMessage(MessageType.EXITING_PS, voterId);
    }

    @Override
    public void openPS() throws InterruptedException {
        sendMessage(MessageType.OPEN_PS);
    }

    @Override
    public void callNextVoter() {
        sendMessage(MessageType.CALL_NEXT_VOTER);
    }

    @Override
    public void closePS() {
        sendMessage(MessageType.CLOSE_PS);
    }

    @Override
    public boolean isEmpty() {
        return boolComm(MessageType.PS_IS_EMPTY);
    }

    @Override
    public boolean isPSclosedAfter() {
        return boolComm(MessageType.PS_IS_CLOSED_AFTER);
    }
    
}