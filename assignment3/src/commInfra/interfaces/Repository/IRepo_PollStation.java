package commInfra.interfaces.Repository;
import java.rmi.*;

/**
 * The IRepo_PollStation interface contains the methods that a repository should implement to interact with the polling station shared region.
 * The repository shared region interacts with the polling station shared region to log the state of the polling station and the voters inside it.
 * The methods in this interface allow the repository to access the polling station shared region.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IRepo_PollStation extends Remote{
    /**
     * The logPollStation method logs the state of the polling station in the repository shared region.
     * @param state The state of the polling station.
     */
    void logPollStation(String state) throws RemoteException;

    /**
     * The logWaiting method logs a voter waiting to enter the polling station in the repository shared region.
     * @param voterId The voter's ID.
     */
    void logWaiting(String voterId) throws RemoteException;

    /**
     * The logInside method logs a voter entering the polling station in the repository shared region.
     * @param voterId The voter's ID.
     */
    void logInside(String voterId) throws RemoteException;

    /**
     * The logExitPoll method logs a voter exiting the polling station and entering the exit poll in the repository shared region.
     * @param voterId The voter's ID.
     */
    void logExitPoll(String voterId) throws RemoteException;
}
