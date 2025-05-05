package Monitors.ExitPoll;
/**
 * The IExitPoll_TPollClerk interface contains the methods that the exit poll shared region
 * should implement to interact with the poll clerk thread.
 */
public interface IExitPoll_TPollClerk {
    /**
     * The close method is called by the poll clerk to close the exit poll.
     */
    void close();
}
