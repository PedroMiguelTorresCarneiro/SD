package serverSide.sharedRegions.PollStation;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * Interface TCP da região partilhada PollStation, dedicada ao tratamento
 * de mensagens provenientes da thread TPollClerk.
 * 
 * Permite abrir/fechar a estação, chamar votantes e verificar o estado da fila.
 * 
 * @author ...
 */
public class IPollStation_TPollClerk {

    private final MPollStation pollStation;

    /**
     * Construtor da interface.
     * @param pollStation instância do monitor MPollStation
     */
    private IPollStation_TPollClerk(MPollStation pollStation) {
        this.pollStation = pollStation;
    }

    public static IPollStation_TPollClerk getInstance(MPollStation pollStation) {
        return new IPollStation_TPollClerk(pollStation);
    }

    /**
     * Processa e responde às mensagens da TPollClerk.
     * 
     * @param inMessage mensagem recebida
     * @return mensagem de resposta
     * @throws MessageException se o tipo ou conteúdo da mensagem for inválido
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case OPEN_PS -> {
                try {
                    openPS();
                    outMessage = Message.getInstance(MessageType.OPEN_PS, RoleType.POLLCLERK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante openPS.", inMessage);
                }
            }

            case CALL_NEXT_VOTER -> {
                callNextVoter();
                outMessage = Message.getInstance(MessageType.CALL_NEXT_VOTER, RoleType.POLLCLERK);
            }

            case CLOSE_PS -> {
                closePS();
                outMessage = Message.getInstance(MessageType.CLOSE_PS, RoleType.POLLCLERK);
            }

            case PS_IS_EMPTY -> {
                boolean empty = isEmpty();
                outMessage = Message.getInstance(MessageType.PS_IS_EMPTY, RoleType.POLLCLERK, empty);
            }

            case PS_IS_CLOSED_AFTER -> {
                boolean closed = isPSclosedAfter();
                outMessage = Message.getInstance(MessageType.PS_IS_CLOSED_AFTER, RoleType.POLLCLERK, closed);
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TPollClerk na PollStation.", inMessage);
        }

        return outMessage;
    }

    private void openPS() throws InterruptedException {
        pollStation.openPS();
    }

    private void callNextVoter() {
        pollStation.callNextVoter();
    }

    private void closePS() {
        pollStation.closePS();
    }

    private boolean isEmpty() {
        return pollStation.isEmpty();
    }

    private boolean isPSclosedAfter() {
        return pollStation.isPSclosedAfter();
    }
}
