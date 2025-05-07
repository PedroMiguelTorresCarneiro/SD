package serverSide.sharedRegions.ExitPoll;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * Interface TCP da região partilhada ExitPoll, dedicada ao tratamento
 * de mensagens provenientes da thread TVoter.
 * 
 * Trata a verificação de abertura, seleção e submissão de voto na sondagem.
 * 
 * @author ...
 */
public class IExitPoll_TVoter {

    private final MExitPoll exitPoll;

    /**
     * Construtor da interface.
     * @param exitPoll instância do monitor ExitPoll
     */
    private IExitPoll_TVoter(MExitPoll exitPoll) {
        this.exitPoll = exitPoll;
    }
    
    public static IExitPoll_TVoter getInstance(MExitPoll exitPoll) {
        return new IExitPoll_TVoter(exitPoll);
    }

    /**
     * Processa e responde a mensagens da TVoter.
     * 
     * @param inMessage mensagem recebida
     * @return mensagem de resposta
     * @throws MessageException se o tipo ou conteúdo da mensagem for inválido
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case PS_IS_OPEN -> {
                boolean open = isOpen();
                outMessage = Message.getInstance(MessageType.PS_IS_OPEN, RoleType.VOTER, open);
            }

            case VOTER_CHOOSEN -> {
                try {
                    boolean selected = choosen();
                    outMessage = Message.getInstance(MessageType.VOTER_CHOOSEN, RoleType.VOTER, selected);
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
                    outMessage = Message.getInstance(MessageType.CALLFORSURVEY, RoleType.VOTER);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante callForSurvey().", inMessage);
                }
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TVoter na ExitPoll.", inMessage);
        }

        return outMessage;
    }

    /**
     * Verifica se a ExitPoll está aberta.
     * @return true se estiver aberta
     */
    private boolean isOpen() {
        return exitPoll.isOpen();
    }

    /**
     * Verifica se o votante foi escolhido para a sondagem.
     * @return true se foi escolhido
     * @throws InterruptedException se a thread for interrompida
     */
    private boolean choosen() throws InterruptedException {
        return exitPoll.choosen();
    }

    /**
     * Submete o voto do inquirido.
     * @param vote voto dado ('A' ou 'B')
     * @param voterId ID do votante
     * @throws InterruptedException se a thread for interrompida
     */
    private void callForSurvey(char vote, String voterId) throws InterruptedException {
        exitPoll.callForSurvey(vote, voterId);
    }
}
