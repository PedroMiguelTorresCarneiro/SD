package serverSide.stubs;

import commInfra.MessageType;
import commInfra.interfaces.Repository.IRepo_ALL;

public class STRepository extends Stub implements IRepo_ALL{
    private static STRepository instance = null;

    private STRepository(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
        System.out.println("Server Repository: " + serverHostName + ":" + serverPortNumb);
    }

    public static STRepository getInstance(String host, int port) {
        instance = new STRepository(host, port);  // SEM if
        return instance;
    }

    @Override
    public void logVoting(String voterId, char vote) {
        sendMessage(MessageType.LOGVOTING, voterId, vote);
    }

    @Override
    public void logElectionResults(long A, long B, String winner) {
        sendMessage(MessageType.LOGELECTIONRESULTS, A, B, winner);
    }

    @Override
    public void close() {
        sendMessage(MessageType.CLOSE);
    }

    @Override
    public void logPollStation(String state) {
        sendMessage(MessageType.LOGPOLL, state);
    }

    @Override
    public void logWaiting(String voterId) {
        sendMessage(MessageType.LOGWAITING, voterId);
    }

    @Override
    public void logInside(String voterId) {
        sendMessage(MessageType.LOGINSIDE, voterId);
    }

    @Override
    public void logExitPoll(String voterId) {
        sendMessage(MessageType.LOGEPOLL, voterId);
    }

    @Override
    public void logIDCheck(String voterId, char accepted) {
        sendMessage(MessageType.LOGID, voterId, accepted);
    }

    @Override
    public void logSurvey(String voterId, char lieOrNot) {
        sendMessage(MessageType.LOGSURVEY, voterId, lieOrNot);
    }

    @Override
    public void logSurveyResults(long A, long B, String winner) {
        sendMessage(MessageType.LOGSURVEYRESULTS, A, B, winner);
    }
    
    public void shutdown() {
        sendMessage(MessageType.SHUTDOWN);
    }
    
}
