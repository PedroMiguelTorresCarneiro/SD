package Monitors.PollStation;

/**
 * The IPollStation_ALL interface contains the methods that the  polling station shared region
 *  should implement to interact with the voters threads and poll clerk thread.
 * This interface extends the IPollStation_TVoter and IPollStation_TPollClerk interfaces.
 * 
 * @see STPollStation_TVoter
 * @see STPollStation_TPollClerk
 * 
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IPollStation_ALL extends IPollStation_TVoter, IPollStation_TPollClerk {
     
}
