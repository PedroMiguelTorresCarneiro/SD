package Monitors.IDCheck;

import Threads.TVoter;

/**
 * The IIDCheck_TVoter interface contains the methods that 
 * an ID check should implement to interact with the voters threads.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IIDCheck_TVoter {
    /**
     * The checkID method is called by the voter threads to check their ID.
     * @param voter The voter thread that is checking its ID.
     * @return boolean Returns true if the voter's ID is valid, false otherwise.
     * @throws InterruptedException Throws an InterruptedException if an error occurs.
     */
    boolean checkID(String voterId) throws InterruptedException;
}
