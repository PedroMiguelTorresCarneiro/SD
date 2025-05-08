package serverSide.sharedRegions.EVotingBooth;

import commInfra.Message;
import commInfra.interfaces.EvotingBooth.IEVotingBooth_ALL;
import commInfra.MessageException;
import commInfra.MessageType;

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
        
        switch (inMessage.getMessageType()){
            case VOTE -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID ausente ou inválido!", inMessage);
                }

                vote(voterId);
                outMessage = Message.getInstance(MessageType.VOTE);
            }
            case GET_VOTE -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID ausente ou inválido!", inMessage);
                }

                char vote = getVote(voterId);
                outMessage = Message.getInstance(MessageType.GET_VOTE, vote);
            }
            case GATHERING_VOTES -> {
                try {
                    gathering();
                    outMessage = Message.getInstance(MessageType.GATHERING_VOTES);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante gathering.", inMessage);
                }
            }

            case PUBLISH_ELECTION_RESULTS -> {
                publishElectionResults();
                outMessage = Message.getInstance(MessageType.PUBLISH_ELECTION_RESULTS);
            }

            case GET_VOTES_COUNT -> {
                int count = getVotesCount();
                outMessage = Message.getInstance(MessageType.GET_VOTES_COUNT, count);
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





}