package Monitors.PollStation;

/**
 * The IPollStation_TVoter interface contains the methods that the polling station shared region
 * should implement to interact with the voter threads.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 * 
 */
public interface IPollStation_TVoter {
    /**
     * The isCLosedAfterElection method is called by the voter to check if the polling station is closed after the election.
     * 
     * @return Boolean The Boolean that indicates if the polling station is closed after the election.
     */
    boolean isCLosedAfterElection();

    /**
     * The canEnterPS method is called by the voter to try to enter in the polling station.
     * 
     * @param voterId The ID of the voter.
     * @return true if the voter entered the polling station, false otherwise.
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     * 
     */
    boolean canEnterPS(String voterId) throws InterruptedException;

    /**
     * The exitingPS method is called by the voter to exit the polling station.
     * @param voterId The ID of the voter.
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    void exitingPS(String voterId) throws InterruptedException;
}
