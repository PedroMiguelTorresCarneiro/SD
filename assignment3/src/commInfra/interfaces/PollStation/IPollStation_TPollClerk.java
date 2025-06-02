package commInfra.interfaces.PollStation;
import java.rmi.*;

/**
 * The IPollStation_TPollClerk interface contains the methods that the polling station shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IPollStation_TPollClerk extends Remote{
    /**
     * The openPS method is called by the poll clerk to open the polling station.
     * 
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    void openPS() throws RemoteException;

    /**
     * The callNextVoter method is called by the poll clerk to call the next voter in the polling station inside queue.
     */
    void callNextVoter() throws RemoteException;

    /**
     * The closePS method is called by the poll clerk to close the polling station.
     */
    void closePS() throws RemoteException;

    /**
     * The isEmpty method is called by the poll clerk to check if the polling station inside queue is empty.
     * 
     * @return boolean The boolean that indicates if the polling station inside queue is empty.
     */
    boolean isEmpty() throws RemoteException;


    /**
     * The isPSclosedAfter method is called by the poll clerk to check if the polling station is closed after the elections.
     * 
     * @return boolean The boolean that indicates if the polling station is closed after the last voter has voted.
     */    
    boolean isPSclosedAfter() throws RemoteException;
    
    void shutdown() throws RemoteException;
}
