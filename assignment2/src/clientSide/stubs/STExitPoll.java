package clientSide.stubs;

import commInfra.MessageType;
import commInfra.interfaces.ExitPoll.IExitPoll_ALL;

public class STExitPoll extends Stub implements IExitPoll_ALL{
    private static STExitPoll instance = null;

    private STExitPoll(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
    }

    public static STExitPoll getInstance(String serverHostName, int serverPortNumb) {
        if (instance == null) {
            instance = new STExitPoll(serverHostName, serverPortNumb);
        }
        return instance;
    }

    @Override
    public boolean choosen() throws InterruptedException {
        return boolComm(MessageType.VOTER_CHOOSEN);
    }

    @Override
    public void callForSurvey(char vote, String voterId) throws InterruptedException {
        sendMessage(MessageType.CALLFORSURVEY, vote, voterId);
    }

    @Override
    public boolean isOpen() {
        return boolComm(MessageType.EP_ISOPEN);
    }

    @Override
    public void conductSurvey() throws InterruptedException {
        sendMessage(MessageType.CONDUCTSURVEY);
    }

    @Override
    public void publishResults() throws InterruptedException {
        sendMessage(MessageType.PUBLISHRESULTS);
    }

    @Override
    public void close() {
        sendMessage(MessageType.CLOSE_PS);
    }
    
    @Override
    public void shutdown() {
        sendMessage(MessageType.SHUTDOWN);
    }

}