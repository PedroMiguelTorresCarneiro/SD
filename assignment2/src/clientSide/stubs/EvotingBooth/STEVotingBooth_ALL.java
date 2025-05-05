package clientSide.stubs.EvotingBooth;

/**
 * The interface IEVotingBooth_ALL contains the methods that the evoting booth shared region
 * should implement to interact with the voters threads and poll clerk thread.
 * 
 * This interface extends the IEVotingBooth_TVoter and IEVotingBooth_TPollClerk interfaces.
 * 
 * @see IEVotingBooth_TVoter
 * @see STEVotingBooth_TPollClerk
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface STEVotingBooth_ALL extends STEVotingBooth_TVoter, STEVotingBooth_TPollClerk{
    
}
