package Monitors.Repository;

/**
 * The IRepo_PollStation interface contains the methods that a repository should implement to interact with the poll station shared region.
 * The repository shared region interacts with the poll station shared region to log the state of the poll station and the voters inside it.
 * The methods in this interface allow the repository to access the poll station shared region.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IRepo_PollStation {
    /**
     * The logPollStation method logs the state of the poll station in the repository shared region.
     * @param state The state of the poll station.
     */
    void logPollStation(String state);

    /**
     * The logWaiting method logs a voter waiting to enter the poll station in the repository shared region.
     * @param voterId The voter's ID.
     */
    void logWaiting(String voterId);

    /**
     * The logInside method logs a voter entering the poll station in the repository shared region.
     * @param voterId The voter's ID.
     */
    void logInside(String voterId);

    /**
     * The logExitPoll method logs a voter exiting the poll station and entering the exit poll in the repository shared region.
     * @param voterId The voter's ID.
     */
    void logExitPoll(String voterId);
}
