package clientSide.stubs.IDCheck;

import clientSide.stubs.Stub;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;

/**
 * The IIDCheck_TVoter interface contains the methods that 
 * an ID check should implement to interact with the voters threads.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class STIDCheck_TVoter extends Stub {
    private static STIDCheck_TVoter instance = null;

    private STIDCheck_TVoter(String host, int port){
        super(host, port);
    }

    public STIDCheck_TVoter getInstance(String host, int port){
        if (instance == null) {
            instance = new STIDCheck_TVoter(host, port);
        }
        
        return instance;
    }

    /**
     * The checkID method is called by the voter threads to check their ID.
     * @param voterId The ID of the voter.
     * @return boolean Returns true if the voter's ID is valid, false otherwise.
     * @throws InterruptedException Throws an InterruptedException if an error occurs.
     */
    public boolean checkID(String voterId) throws InterruptedException{
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.CHECK_ID);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /* 
            logic to handle the response from the server
        */

        com.close();
        
        return inMessage.getVoterIdCheckResult();
    };
}
