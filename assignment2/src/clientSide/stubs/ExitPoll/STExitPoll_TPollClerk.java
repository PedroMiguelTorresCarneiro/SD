package clientSide.stubs.ExitPoll;

import clientSide.stubs.Stub;
import commInfra.MessageType;
import commInfra.RoleType;


/**
 * The IExitPoll_TPollClerk interface contains the methods that the exit poll shared region
 * should implement to interact with the poll clerk thread.
 */
public class STExitPoll_TPollClerk extends Stub{
    private static STExitPoll_TPollClerk instance = null;

    private STExitPoll_TPollClerk(String host, int port){
        super(host, port);
    }


    public static STExitPoll_TPollClerk getInstance(String host, int port){
        if (instance == null) {
            instance = new STExitPoll_TPollClerk(host, port);
        }
        
        return instance;
    }

    /**
     * The close method is called by the poll clerk to close the exit poll.
     */
    public void close(){
        sendMessage(MessageType.CLOSE_PS, RoleType.POLLCLERK);
    }
}
