package serverSide.sharedRegions.ExitPoll;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * Interface TCP da região partilhada ExitPoll, dedicada ao tratamento
 * de mensagens provenientes da thread TPollClerk.
 * 
 * Trata a operação de fecho da sondagem à boca das urnas.
 * 
 * @author ...
 */
public class IExitPoll_TPollClerk {

    private final MExitPoll exitPoll;

    /**
     * Construtor da interface.
     * @param exitPoll instância do monitor ExitPoll
     */
    private IExitPoll_TPollClerk(MExitPoll exitPoll) {
        this.exitPoll = exitPoll;
    }

    public static IExitPoll_TPollClerk getInstance(MExitPoll exitPoll) {
        return new IExitPoll_TPollClerk(exitPoll);
    }

    /**
     * Processa e responde a mensagens da TPollClerk.
     * 
     * @param inMessage mensagem recebida
     * @return mensagem de resposta
     * @throws MessageException se o tipo de mensagem for inválido
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case CLOSE_PS -> {
                close();
                outMessage = Message.getInstance(MessageType.CLOSE_PS, RoleType.POLLCLERK);
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TPollClerk na ExitPoll.", inMessage);
        }

        return outMessage;
    }

    /**
     * Método invocado pela TPollClerk para encerrar a Exit Poll.
     */
    private void close() {
        exitPoll.close();
    }
}
