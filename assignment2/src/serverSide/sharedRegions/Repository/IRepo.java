package serverSide.sharedRegions.Repository;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.interfaces.Repository.IRepo_ALL;


public class IRepo implements IRepo_ALL{
    private static IRepo instance = null;
    private final MRepo repo;
    
    private IRepo(MRepo repo){
        this.repo = repo;
    }
    
    public static IRepo getInstance(MRepo repo){
        if (instance == null) {
            instance = new IRepo(repo);
        }
        return instance;
    }
    
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMsgType()) {
            case LOGVOTING -> {
                System.out.println("\nCASE LOGVOTING --->\n");
                logVoting(inMessage.getInfo(), inMessage.getCaracter());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGELECTIONRESULTS -> {
                System.out.println("\nCASE LOGELECTIONRESULTS --->\n");
                logElectionResults(inMessage.getLongA(), inMessage.getLongB(), inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case CLOSE -> {
                System.out.println("\nCASE CLOSE --->\n");
                close();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGPOLL -> {
                System.out.println("\nCASE LOGPOLL --->\n");
                logPollStation(inMessage.getInfo());
                System.out.println("Poll LOGGED");
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGWAITING -> {
                System.out.println("\nCASE LOGWAITING --->\n");
                logWaiting(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGINSIDE -> {
                System.out.println("\nCASE LOGINSIDE --->\n");
                logInside(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGEPOLL -> {
                System.out.println("\nCASE LOGEPOLL --->\n");
                logExitPoll(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGID -> {
                System.out.println("\nCASE LOGID --->\n");
                logIDCheck(inMessage.getInfo(), inMessage.getCaracter());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGSURVEY -> {
                System.out.println("\nCASE LOGSURVEY --->\n");
                logSurvey(inMessage.getInfo(), inMessage.getCaracter());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGSURVEYRESULTS -> {
                System.out.println("\nCASE LOGSURVEYRESULTS --->\n");
                logSurveyResults(inMessage.getLongA(), inMessage.getLongB(), inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            default -> throw new MessageException("Tipo de mensagem inválido", inMessage);
        }

        return outMessage;
    }

    
    @Override
    public void logVoting(String voterId, char vote) {
        repo.logVoting(voterId, vote);
    }

    @Override
    public void logElectionResults(long A, long B, String winner) {
        repo.logElectionResults(A, B, winner);
    }

    @Override
    public void close() {
        repo.close();
    }

    @Override
    public void logPollStation(String state) {
        repo.logPollStation(state);
    }

    @Override
    public void logWaiting(String voterId) {
        repo.logWaiting(voterId);
    }

    @Override
    public void logInside(String voterId) {
        repo.logInside(voterId);
    }

    @Override
    public void logExitPoll(String voterId) {
        repo.logExitPoll(voterId);
    }

    @Override
    public void logIDCheck(String voterId, char accepted) {
        repo.logIDCheck(voterId, accepted);
    }

    @Override
    public void logSurvey(String voterId, char lieOrNot) {
        repo.logSurvey(voterId, lieOrNot);
    }

    @Override
    public void logSurveyResults(long A, long B, String winner) {
        repo.logSurveyResults(A, B, winner);
    }   
}
