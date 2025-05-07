package serverSide.sharedRegions.ExitPoll;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * Interface TCP da região partilhada ExitPoll, dedicada ao tratamento
 * de mensagens provenientes da thread TPollster.
 * 
 * Trata a verificação de estado, condução do inquérito e publicação dos resultados.
 * 
 * @author ...
 */
public class IExitPoll_TPollster {

    private final MExitPoll exitPoll;

    /**
     * Construtor da interface.
     * @param exitPoll instância do monitor ExitPoll
     */
    private IExitPoll_TPollster(MExitPoll exitPoll) {
        this.exitPoll = exitPoll;
    }

    public static IExitPoll_TPollster getInstance(MExitPoll exitPoll) {
        return new IExitPoll_TPollster(exitPoll);
    }


    /**
     * Processa e responde a mensagens da TPollster.
     * 
     * @param inMessage mensagem recebida
     * @return mensagem de resposta
     * @throws MessageException se o tipo de mensagem for inválido
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case EP_ISOPEN -> {
                boolean isOpen = isOpen();
                outMessage = Message.getInstance(MessageType.EP_ISOPEN, RoleType.POLLSTER, isOpen);
            }

            case CONDUCTSURVEY -> {
                try {
                    conductSurvey();
                    outMessage = Message.getInstance(MessageType.CONDUCTSURVEY, RoleType.POLLSTER);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante conductSurvey.", inMessage);
                }
            }

            case PUBLISHRESULTS -> {
                try {
                    publishResults();
                    outMessage = Message.getInstance(MessageType.PUBLISHRESULTS, RoleType.POLLSTER);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante publishResults.", inMessage);
                }
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TPollster na ExitPoll.", inMessage);
        }

        return outMessage;
    }

    /**
     * Verifica se a Exit Poll está aberta.
     * @return true se estiver aberta, false caso contrário
     */
    private boolean isOpen() {
        return exitPoll.isOpen();
    }

    /**
     * Conduz o inquérito a um votante.
     * @throws InterruptedException se a thread for interrompida
     */
    private void conductSurvey() throws InterruptedException {
        exitPoll.conductSurvey();
    }

    /**
     * Publica os resultados do inquérito.
     * @throws InterruptedException se a thread for interrompida
     */
    private void publishResults() throws InterruptedException {
        exitPoll.publishResults();
    }
}
