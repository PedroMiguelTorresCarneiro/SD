package Monitors.ExitPoll;

/**
 * The IExitPoll_ALL interface contains the methods that the exit poll shared region
 * should implement to interact with the voters threads, pollster thread and poll clerk thread.
 * 
 * This interface extends the IExitPoll_TVoter, IExitPoll_TPollster and IExitPoll_TPollClerk interfaces.    
 * 
 * @see IExitPoll_TVoter
 * @see IExitPoll_TPollster
 * @see IExitPoll_TPollClerk
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IExitPoll_ALL extends IExitPoll_TVoter, IExitPoll_TPollster, IExitPoll_TPollClerk{
    
}
