package Monitors.PollStation;

/**
 * The IPollStation_TPollClerk interface contains the methods that the polling station shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IPollStation_TPollClerk {
    /**
     * The openPS method is called by the poll clerk to open the polling station.
     * 
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    void openPS() throws InterruptedException;

    /**
     * The callNextVoter method is called by the poll clerk to call the next voter in the polling station inside queue.
     */
    void callNextVoter();

    /**
     * The closePS method is called by the poll clerk to close the polling station.
     */
    void closePS();

    /**
     * The isEmpty method is called by the poll clerk to check if the polling station inside queue is empty.
     * 
     * @return boolean The boolean that indicates if the polling station inside queue is empty.
     */
    boolean isEmpty();


    /**
     * The isPSclosedAfter method is called by the poll clerk to check if the polling station is closed after the elections.
     * 
     * @return boolean The boolean that indicates if the polling station is closed after the last voter has voted.
     */    
    public boolean isPSclosedAfter();
}
