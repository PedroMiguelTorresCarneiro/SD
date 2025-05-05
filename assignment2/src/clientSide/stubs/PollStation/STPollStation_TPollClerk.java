package clientSide.stubs.PollStation;
import clientSide.stubs.Stub;

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
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.OPENPS, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();
    };

    /**
     * The callNextVoter method is called by the poll clerk to call the next voter in the polling station inside queue.
     */
    public void callNextVoter(){
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.CALLNEXTVOTER, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();
    }

    /**
     * The closePS method is called by the poll clerk to close the polling station.
     */
    public void closePS(){
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.CLOSEPS, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();
    };

    /**
     * The isEmpty method is called by the poll clerk to check if the polling station inside queue is empty.
     * 
     * @return boolean The boolean that indicates if the polling station inside queue is empty.
     */
    public boolean isEmpty(){
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.ISEMPTY, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();

        return inMessage.getBooleanValue();
    };

    /**
     * The maxVotes method is called by the poll clerk to check if the maximum number of votes has been reached.
     * 
     * @param maxVotes The maximum number of votes.
     * @param maxVoters The maximum number of voters.
     * @return boolean The boolean that indicates if the maximum number of votes has been reached.
     */
    public boolean maxVotes(int maxVotes, int maxVoters) {
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.MAXVOTES, new Object[]{maxVotes, maxVoters});
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();

        return inMessage.getBooleanValue();
    };
    
    
    public boolean isPSclosedAfter(){
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.ISPSCLOSED, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();

        return inMessage.getBooleanValue();
    };
}
