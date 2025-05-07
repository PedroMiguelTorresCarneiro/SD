package serverSide.sharedRegions.IDCheck;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * Interface TCP da região partilhada IDCheck, dedicada ao tratamento
 * de mensagens provenientes da thread TVoter.
 * 
 * Permite simular a verificação de identidade do votante.
 * 
 * @author ...
 */
public class IIDCheck_TVoter {

    private final MIDCheck idCheck;

    /**
     * Construtor da interface.
     * @param idCheck instância do monitor MIDCheck
     */
    private IIDCheck_TVoter(MIDCheck idCheck) {
        this.idCheck = idCheck;
    }

    public static IIDCheck_TVoter getInstance(MIDCheck idCheck) {
        return new IIDCheck_TVoter(idCheck);
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
            case CHECK_ID -> {
                String voterId = inMessage.getVoterId();

                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente!", inMessage);
                }

                try {
                    boolean isValid = checkID(voterId);
                    outMessage = Message.getInstance(MessageType.CHECK_ID, RoleType.VOTER, isValid);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante checkID().", inMessage);
                }
            }

            default -> throw new MessageException("Tipo de mensagem inválido para TVoter na IDCheck.", inMessage);
        }

        return outMessage;
    }

    /**
     * Método invocado pela TVoter para verificar o seu ID.
     * 
     * @param voterId ID do votante
     * @return true se for aceite, false caso contrário
     * @throws InterruptedException se a thread for interrompida
     */
    private boolean checkID(String voterId) throws InterruptedException {
        return idCheck.checkID(voterId);
    }
}
