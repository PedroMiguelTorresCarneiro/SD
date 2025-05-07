package serverSide.sharedRegions.EVotingBooth;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * The IEVotingBooth_TPollClerk interface contains the methods that the evoting booth shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class IEVotingBooth_TPollClerk {
    private final MEvotingBooth booth;

    private IEVotingBooth_TPollClerk(MEvotingBooth booth) {
        this.booth = booth;
    }

    public static IEVotingBooth_TPollClerk getInstance(MEvotingBooth booth) {
        return new IEVotingBooth_TPollClerk(booth);
    }

    /**
     * Processa e responde às mensagens recebidas da thread TPollClerk.
     * 
     * @param inMessage mensagem recebida
     * @return mensagem de resposta
     * @throws MessageException se o tipo de mensagem for inválido
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case GATHERING_VOTES -> {
                try {
                    gathering();
                    outMessage = Message.getInstance(MessageType.GATHERING_VOTES, RoleType.POLLCLERK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante gathering.", inMessage);
                }
            }

            case PUBLISH_ELECTION_RESULTS -> {
                publishElectionResults();
                outMessage = Message.getInstance(MessageType.PUBLISH_ELECTION_RESULTS, RoleType.POLLCLERK);
            }

            case GET_VOTES_COUNT -> {
                int count = getVotesCount();
                outMessage = Message.getInstance(MessageType.GET_VOTES_COUNT, RoleType.POLLCLERK, count);
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TPollClerk.", inMessage);
        }

        return outMessage;
    }

    /**
     * Método chamado para recolher todos os votos.
     * @throws InterruptedException se a thread for interrompida
     */
    private void gathering() throws InterruptedException {
        booth.gathering();
    }

    /**
     * Método chamado para publicar os resultados das eleições.
     */
    private void publishElectionResults() {
        booth.publishElectionResults();
    }

    /**
     * Método chamado para obter o número de votos recolhidos.
     * @return número total de votos registados
     */
    private int getVotesCount() {
        return booth.getVotesCount();
    }
}
