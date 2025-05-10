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
        
        switch (inMessage.getMsgType()){
            case CLOSE_PS -> {
                System.out.println("\n[EXITPOLL] CASE CLOSE_PS --->");
                System.out.println("Closing exit poll...");
                close();
                System.out.println("Exit poll closed.");
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case EP_ISOPEN -> {
                System.out.println("\n[EXITPOLL] CASE EP_ISOPEN --->");
                System.out.println("Checking if exit poll is open...");
                boolean isOpen = isOpen();
                System.out.println("Exit poll is open: " + isOpen);
                outMessage = Message.getInstance(MessageType.EP_ISOPEN, isOpen);
            }
            case CONDUCTSURVEY -> {
                System.out.println("\n[EXITPOLL] CASE CONDUCTSURVEY --->");
                try {
                    System.out.println("Conducting survey...");
                    conductSurvey();
                    System.out.println("Survey conducted.");
                    outMessage = Message.getInstance(MessageType.ACK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante conductSurvey.", inMessage);
                }
            }
            case PUBLISHRESULTS -> {
                System.out.println("\n[EXITPOLL] CASE PUBLISHRESULTS --->");
                try {
                    System.out.println("Publishing results...");
                    publishResults();
                    System.out.println("Results published.");
                    outMessage = Message.getInstance(MessageType.ACK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante publishResults.", inMessage);
                }
            }
            case VOTER_CHOOSEN -> {
                System.out.println("\n[EXITPOLL] CASE VOTER_CHOOSEN --->");
                try {
                    System.out.println("Waiting for voter to choose...");
                    boolean selected = choosen();
                    System.out.println("Voter has chosen: " + selected);
                    outMessage = Message.getInstance(MessageType.VOTER_CHOOSEN, selected);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante choosen().", inMessage);
                }
            }
            case CALLFORSURVEY -> {
                System.out.println("\n[EXITPOLL] CASE CALLFORSURVEY --->");
                System.out.println("Calling for survey...");
                char vote = inMessage.getCaracter();
                System.out.println("Vote: " + vote);
                String voterId = inMessage.getInfo();

                if (voterId == null || voterId.isEmpty())
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);

                if (vote != 'A' && vote != 'B')
                    throw new MessageException("Voto inválido: " + vote, inMessage);

                try {
                    System.out.println("Calling for survey with vote: " + vote + " and voter ID: " + voterId);
                    callForSurvey(vote, voterId);
                    System.out.println("Survey called.");
                    outMessage = Message.getInstance(MessageType.ACK);
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
