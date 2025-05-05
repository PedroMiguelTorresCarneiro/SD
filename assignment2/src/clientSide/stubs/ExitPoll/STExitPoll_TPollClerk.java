package clientSide.stubs.ExitPoll;
import clientSide.stubs.Stub;
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
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.CLOSE, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();

    };
}
