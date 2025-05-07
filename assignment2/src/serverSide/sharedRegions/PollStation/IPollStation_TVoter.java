package serverSide.sharedRegions.PollStation;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * Interface TCP da região partilhada PollStation, dedicada ao tratamento
 * de mensagens provenientes da thread TVoter.
 * 
 * Permite ao votante entrar, sair e verificar se a estação foi encerrada.
 * 
 * @author ...
 */
public class IPollStation_TVoter {

    private final MPollStation pollStation;

    /**
     * Construtor da interface.
     * @param pollStation instância do monitor MPollStation
     */
    private IPollStation_TVoter(MPollStation pollStation) {
        this.pollStation = pollStation;
    }

    public static IPollStation_TVoter getInstance(MPollStation pollStation) {
        return new IPollStation_TVoter(pollStation);
    }

    /**
     * Processa e responde às mensagens da TVoter.
     * 
     * @param inMessage mensagem recebida
     * @return mensagem de resposta
     * @throws MessageException se o tipo ou conteúdo da mensagem for inválido
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case CAN_ENTER_PS -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);
                }

                try {
                    boolean entered = canEnterPS(voterId);
                    outMessage = Message.getInstance(MessageType.CAN_ENTER_PS, RoleType.VOTER, entered);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante canEnterPS.", inMessage);
                }
            }

            case PS_IS_CLOSED_AFTER -> {
                boolean closed = isCLosedAfterElection();
                outMessage = Message.getInstance(MessageType.PS_IS_CLOSED_AFTER, RoleType.VOTER, closed);
            }

            case EXITING_PS -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);
                }

                try {
                    exitingPS(voterId);
                    outMessage = Message.getInstance(MessageType.EXITING_PS, RoleType.VOTER);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante exitingPS.", inMessage);
                }
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TVoter na PollStation.", inMessage);
        }

        return outMessage;
    }

    /**
     * Votante tenta entrar na estação de voto.
     */
    private boolean canEnterPS(String voterId) throws InterruptedException {
        return pollStation.canEnterPS(voterId);
    }

    /**
     * Votante verifica se a estação foi encerrada após eleições.
     */
    private boolean isCLosedAfterElection() {
        return pollStation.isCLosedAfterElection();
    }

    /**
     * Votante abandona a estação e dirige-se à exit poll.
     */
    private void exitingPS(String voterId) throws InterruptedException {
        pollStation.exitingPS(voterId);
    }
}
