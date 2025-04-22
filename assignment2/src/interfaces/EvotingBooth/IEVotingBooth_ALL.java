package interfaces.EvotingBooth;

/**
 * The interface IEVotingBooth_ALL contains the methods that the evoting booth shared region
 * should implement to interact with the voters threads and poll clerk thread.
 * 
 * This interface extends the IEVotingBooth_TVoter and IEVotingBooth_TPollClerk interfaces.
 * 
 * @see IEVotingBooth_TVoter
 * @see IEVotingBooth_TPollClerk
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IEVotingBooth_ALL extends IEVotingBooth_TVoter, IEVotingBooth_TPollClerk{
    
}
