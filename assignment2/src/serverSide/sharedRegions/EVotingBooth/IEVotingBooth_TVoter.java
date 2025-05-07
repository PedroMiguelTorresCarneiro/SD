package serverSide.sharedRegions.EVotingBooth;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * The IEVotingBooth_TVoter interface contains the methods that the evoting booth shared region
 * should implement to interact with the voter threads.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro   
 */
public class IEVotingBooth_TVoter{
    private final MEvotingBooth booth;

    private IEVotingBooth_TVoter(MEvotingBooth booth) {
        this.booth = booth;
    }

    public static IEVotingBooth_TVoter getInstance(MEvotingBooth booth) {
        return new IEVotingBooth_TVoter(booth);
    }

    public Message processAndReply(Message inMessage) throws MessageException, InterruptedException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case VOTE -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID ausente ou inválido!", inMessage);
                }

                vote(voterId);
                outMessage = Message.getInstance(MessageType.VOTE, RoleType.VOTER);
            }
            case GET_VOTE -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID ausente ou inválido!", inMessage);
                }

                char vote = getVote(voterId);
                outMessage = Message.getInstance(MessageType.GET_VOTE, RoleType.VOTER, vote);
            }
            default -> throw new MessageException("Invalid message type", inMessage);
            
        }
        return outMessage;
    }


    /**
     * The vote method is called by the voter to vote.
     * @param voterId the voter id.
     * @throws InterruptedException if the thread is interrupted.
     */
    private void vote(String voterId) throws InterruptedException{
        booth.vote(voterId);
    }

   /**
    * The getVote method is called by the voter to get the vote.
    * @param voterId the voter id.
    * @return the vote of the voter.
    */
    private char getVote(String voterId){
        return booth.getVote(voterId);
    }
}
