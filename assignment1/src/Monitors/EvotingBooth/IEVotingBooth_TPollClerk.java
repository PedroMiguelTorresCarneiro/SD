package stubs.EvotingBooth;

/**
 * The IEVotingBooth_TPollClerk interface contains the methods that the evoting booth shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IEVotingBooth_TPollClerk {
    /**
     * The gathering method is called by the poll clerk to gather all the votes.
     * @throws InterruptedException if the thread is interrupted.
     */
    void gathering() throws InterruptedException;
    
    /**
     * The publishElectionResults method is called by the poll clerk to publish the election results.
     */
    void publishElectionResults();
    
    /**
     * The getVotesCount method is called by the poll clerk to get the number of voters.
     * @return the number of voters that have voted.
     */
    int getVotesCount();
}
