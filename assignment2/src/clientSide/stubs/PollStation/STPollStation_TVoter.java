package clientSide.stubs.PollStation;
import clientSide.stubs.Stub;
import commInfra.MessageType;

/**
 * The IPollStation_TVoter interface contains the methods that the polling station shared region
 * should implement to interact with the voter threads.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 * 
 */
public class STPollStation_TVoter extends Stub {
    private static STPollStation_TVoter instance = null;

    private STPollStation_TVoter(String host, int port) {
        super(host, port);
    }

    public static STPollStation_TVoter getInstance(String host, int port) {
        if (instance == null) {
            instance = new STPollStation_TVoter(host, port);
        }
        
        return instance;
    }

    /**
     * The isCLosedAfterElection method is called by the voter to check if the polling station is closed after the election.
     * 
     * @return Boolean The Boolean that indicates if the polling station is closed after the election.
     */
    public boolean isCLosedAfterElection(){
        return boolComm(MessageType.PS_IS_CLOSED_AFTER);
    }

    /**
     * The canEnterPS method is called by the voter to try to enter in the polling station.
     * 
     * @param voterId The ID of the voter.
     * @return true if the voter entered the polling station, false otherwise.
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     * 
     */
    public boolean canEnterPS(String voterId) throws InterruptedException{
        return boolComm(MessageType.CAN_ENTER_PS, voterId);
    }

    /**
     * The exitingPS method is called by the voter to exit the polling station.
     * @param voterId The ID of the voter.
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    public void exitingPS(String voterId) throws InterruptedException{
        sendMessage(MessageType.EXITING_PS, voterId);
    }
}
