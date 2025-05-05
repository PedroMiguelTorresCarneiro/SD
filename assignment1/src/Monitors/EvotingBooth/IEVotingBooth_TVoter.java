package stubs.EvotingBooth;

/**
 * The IEVotingBooth_TVoter interface contains the methods that the evoting booth shared region
 * should implement to interact with the voter threads.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro   
 */
public interface IEVotingBooth_TVoter{
    /**
     * The vote method is called by the voter to vote.
     * @param voterId the voter id.
     * @throws InterruptedException if the thread is interrupted.
     */
    void vote(String voterId) throws InterruptedException;

   /**
    * The getVote method is called by the voter to get the vote.
    * @param voterId the voter id.
    * @return the vote of the voter.
    */
    Character getVote(String voterId);
}
