package serverSide.sharedRegions.ExitPoll;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.interfaces.ExitPoll.IExitPoll_ALL;

/**
 *
 * @author pedrocarneiro
 */
public class IExitPoll implements IExitPoll_ALL {
    private static IExitPoll instance = null;
    private final MExitPoll exitPoll;
    
    private IExitPoll(MExitPoll exitPoll){
        this.exitPoll = exitPoll;
    }
    
    public static IExitPoll getInstance(MExitPoll exitPoll) {
        if (instance == null) {
            instance = new IExitPoll(exitPoll);
        }
        return instance;
    }
    
    public Message processAndReply(Message inMessage) throws MessageException, InterruptedException{
        Message outMessage;
        
        switch (inMessage.getMessageType()){
            case CLOSE_PS -> {
                close();
                outMessage = Message.getInstance(MessageType.CLOSE_PS);
            }
            case EP_ISOPEN -> {
                boolean isOpen = isOpen();
                outMessage = Message.getInstance(MessageType.EP_ISOPEN, isOpen);
            }
            case CONDUCTSURVEY -> {
                try {
                    conductSurvey();
                    outMessage = Message.getInstance(MessageType.CONDUCTSURVEY);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante conductSurvey.", inMessage);
                }
            }
            case PUBLISHRESULTS -> {
                try {
                    publishResults();
                    outMessage = Message.getInstance(MessageType.PUBLISHRESULTS);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante publishResults.", inMessage);
                }
            }
            case VOTER_CHOOSEN -> {
                try {
                    boolean selected = choosen();
                    outMessage = Message.getInstance(MessageType.VOTER_CHOOSEN, selected);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante choosen().", inMessage);
                }
            }
            case CALLFORSURVEY -> {
                char vote = inMessage.getAnswerType2();
                String voterId = inMessage.getVoterId();

                if (voterId == null || voterId.isEmpty())
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);

                if (vote != 'A' && vote != 'B')
                    throw new MessageException("Voto inválido: " + vote, inMessage);

                try {
                    callForSurvey(vote, voterId);
                    outMessage = Message.getInstance(MessageType.CALLFORSURVEY);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante callForSurvey().", inMessage);
                }
            }
            default -> throw new MessageException("Tipo de mensagem inválido", inMessage);
        }
        
        return outMessage;
    }
    
    
    @Override
    public boolean choosen() throws InterruptedException {
        return exitPoll.choosen();
    }

    @Override
    public boolean isOpen() {
        return exitPoll.isOpen();
    }

    @Override
    public void conductSurvey() throws InterruptedException {
        exitPoll.conductSurvey();
    }

    @Override
    public void publishResults() throws InterruptedException {
        exitPoll.publishResults();
    }

    @Override
    public void close() {
        exitPoll.close();
    }

    @Override
    public void callForSurvey(char vote, String voterId) throws InterruptedException {
        exitPoll.callForSurvey(vote, voterId);
    }
    
}
