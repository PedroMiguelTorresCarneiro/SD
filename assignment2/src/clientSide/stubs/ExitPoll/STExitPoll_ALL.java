package clientSide.stubs.ExitPoll;

/**
 * The IExitPoll_ALL interface contains the methods that the exit poll shared region
 * should implement to interact with the voters threads, pollster thread and poll clerk thread.
 * 
 * This interface extends the IExitPoll_TVoter, IExitPoll_TPollster and IExitPoll_TPollClerk interfaces.    
 * 
 * @see STExitPoll_TVoter
 * @see STExitPoll_TPollster
 * @see STExitPoll_TPollClerk
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface STExitPoll_ALL extends STExitPoll_TVoter, STExitPoll_TPollster, STExitPoll_TPollClerk{
    
}
