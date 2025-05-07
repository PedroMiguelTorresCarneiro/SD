package clientSide.stubs.PollStation;
import clientSide.stubs.Stub;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * The IPollStation_TPollClerk interface contains the methods that the polling station shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class STPollStation_TPollClerk extends Stub {
    private static STPollStation_TPollClerk instance = null;

    private STPollStation_TPollClerk(String host, int port) {
        super(host, port);
    }

    public static STPollStation_TPollClerk getInstance(String host, int port) {
        if (instance == null) {
            instance = new STPollStation_TPollClerk(host, port);
        }
        
        return instance;
    }

    /**
     * The openPS method is called by the poll clerk to open the polling station.
     * 
     * @throws InterruptedException The exception thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    public void openPS() throws InterruptedException{
        sendMessage(MessageType.OPEN_PS, RoleType.POLLCLERK);
    };

    /**
     * The callNextVoter method is called by the poll clerk to call the next voter in the polling station inside queue.
     */
    public void callNextVoter(){
        sendMessage(MessageType.CALL_NEXT_VOTER, RoleType.POLLCLERK);
    }

    /**
     * The closePS method is called by the poll clerk to close the polling station.
     */
    public void closePS(){
        sendMessage(MessageType.CLOSE_PS, RoleType.POLLCLERK);
    };

    /**
     * The isEmpty method is called by the poll clerk to check if the polling station inside queue is empty.
     * 
     * @return boolean The boolean that indicates if the polling station inside queue is empty.
     */
    public boolean isEmpty(){
        return boolComm(MessageType.PS_IS_EMPTY, RoleType.POLLCLERK);
    };

        
    public boolean isPSclosedAfter(){
        return boolComm(MessageType.PS_IS_CLOSED_AFTER, RoleType.POLLCLERK);
    };
}
