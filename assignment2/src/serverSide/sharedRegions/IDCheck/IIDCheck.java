package serverSide.sharedRegions.IDCheck;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.interfaces.IDCheck.IIDCheck_ALL;

/**
 * Interface TCP da região partilhada IDCheck, dedicada ao tratamento
 * de mensagens provenientes da thread TVoter.
 * 
 * Permite simular a verificação de identidade do votante.
 * 
 * @author ...
 */
public class IIDCheck implements IIDCheck_ALL{
    private static IIDCheck instance = null;
    private final MIDCheck idCheck;

    /**
     * Construtor da interface.
     * @param idCheck instância do monitor MIDCheck
     */
    private IIDCheck(MIDCheck idCheck) {
        this.idCheck = idCheck;
    }

    public static IIDCheck getInstance(MIDCheck idCheck) {
        if (instance == null) {
            instance = new IIDCheck(idCheck);
        }
        return instance;
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

        switch (inMessage.getMsgType()) {
            case CHECK_ID -> {
                System.out.println("\n[IDCheck] CASE CHECK_ID --->");
                String voterId = inMessage.getInfo();

                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente!", inMessage);
                }

                try {
                    System.out.println("[IDCheck] A verificar ID do votante: " + voterId);
                    boolean isValid = checkID(voterId);
                    System.out.println("[IDCheck] ID do votante " + (isValid ? "aceite" : "não aceite"));
                    outMessage = Message.getInstance(MessageType.CHECK_ID, isValid);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante checkID().", inMessage);
                }
            }
            case SHUTDOWN -> {
                System.out.println("🔻 Pedido de shutdown recebido.");
                outMessage = Message.getInstance(MessageType.ACK);  // responde primeiro!
                new Thread(() -> {
                    try {
                        Thread.sleep(100); // pequeno delay para garantir que o ACK é enviado
                        shutdown(); // encerra depois
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            default -> throw new MessageException("Tipo de mensagem inválido", inMessage);
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
    @Override
    public boolean checkID(String voterId) throws InterruptedException {
        return idCheck.checkID(voterId);
    }

    @Override
    public void shutdown() {
        System.exit(0);  // Encerra este servidor
    }
}
