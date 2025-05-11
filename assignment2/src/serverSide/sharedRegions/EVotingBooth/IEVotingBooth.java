package serverSide.sharedRegions.EVotingBooth;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.interfaces.EvotingBooth.IEVotingBooth_ALL;

public class IEVotingBooth implements IEVotingBooth_ALL {
    private static IEVotingBooth instance = null;
    private final MEvotingBooth booth;
    
    private IEVotingBooth(MEvotingBooth booth) {
        this.booth = booth;
    }

    public static IEVotingBooth getInstance(MEvotingBooth booth) {
        if (instance == null) {
            instance = new IEVotingBooth(booth);
        }
        return instance;
    }
    
    public Message processAndReply(Message inMessage) throws MessageException, InterruptedException{
        Message outMessage;
        
        switch (inMessage.getMsgType()){
            case VOTE -> {
                System.out.println("\n[EVotingBooth] CASE VOTE --->");
                String voterId = inMessage.getInfo();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID ausente ou inválido!", inMessage);
                }
                System.out.println("Voter ID: " + voterId);
                vote(voterId);
                System.out.println("Voto registrado com sucesso!");
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case GET_VOTE -> {
                System.out.println("\n[EVotingBooth] CASE GET_VOTE --->");
                String voterId = inMessage.getInfo();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID ausente ou inválido!", inMessage);
                }
                System.out.println("Voter ID: " + voterId);
                char vote = getVote(voterId);
                System.out.println("Voto "+ vote + " recuperado com sucesso!");
                outMessage = Message.getInstance(MessageType.GET_VOTE, vote);
            }
            case GATHERING_VOTES -> {
                System.out.println("\n[EVotingBooth] CASE GATHERING_VOTES --->");
                try {
                    System.out.println("Iniciando o gathering...");
                    gathering();
                    System.out.println("Gathering concluído com sucesso!");
                    outMessage = Message.getInstance(MessageType.ACK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante gathering.", inMessage);
                }
            }

            case PUBLISH_ELECTION_RESULTS -> {
                System.out.println("\n[EVotingBooth] CASE PUBLISH_ELECTION_RESULTS --->");
                System.out.println("Publicando resultados da eleição...");
                publishElectionResults();
                System.out.println("Resultados publicados com sucesso!");
                outMessage = Message.getInstance(MessageType.ACK);
            }

            case GET_VOTES_COUNT -> {
                System.out.println("\n[EVotingBooth] CASE GET_VOTES_COUNT --->");
                System.out.println("Contando votos...");
                int count = getVotesCount();
                System.out.println("Total de votos: " + count);
                outMessage = Message.getInstance(MessageType.GET_VOTES_COUNT, count);
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

    @Override
    public void vote(String voterId) throws InterruptedException {
        booth.vote(voterId);
    }

    @Override
    public char getVote(String voterId) {
        return booth.getVote(voterId);
    }

    @Override
    public void gathering() throws InterruptedException {
        booth.gathering();
    }

    @Override
    public void publishElectionResults() {
        booth.publishElectionResults();
    }

    @Override
    public int getVotesCount() {
        return booth.getVotesCount();
    }

    @Override
    public void shutdown() {
        System.exit(0);  // Encerra este servidor
    }





}