package clientSide.stubs.EvotingBooth;

import clientSide.stubs.Stub;
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
public class STEVotingBooth_TVoter extends Stub{
    private static STEVotingBooth_TVoter instance = null;

    private STEVotingBooth_TVoter(String host, int port){
        super(host, port);
    }

    public static STEVotingBooth_TVoter getInstance(String host, int port){
        if (instance == null) {
            instance = new STEVotingBooth_TVoter(host, port);
        }
        
        return instance;
    }
    /**
     * The vote method is called by the voter to vote.
     * @param voterId the voter id.
     * @throws InterruptedException if the thread is interrupted.
     */
    public void vote(String voterId) throws InterruptedException{
        sendMessage(MessageType.VOTE, RoleType.VOTER, voterId);
    };

   /**
    * The getVote method is called by the voter to get the vote.
    * @param voterId the voter id.
    * @return the vote of the voter.
    */
    public Character getVote(String voterId){
        return charComm(MessageType.GET_VOTE, RoleType.VOTER, voterId);
    };
}
