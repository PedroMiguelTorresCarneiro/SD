package clientSide.stubs.ExitPoll;
import clientSide.stubs.Stub;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;

/**
 * The IExitPoll_TPollster interface contains the methods that the exit poll shared region
 * should implement to interact with the pollster thread.
 */

public class STExitPoll_TPollster extends Stub{
    private static STExitPoll_TPollster instance = null;

    private STExitPoll_TPollster(String host, int port) {
       super(host, port);
    }

    public static STExitPoll_TPollster getInstance(String host, int port) {
        if (instance == null) {
            instance = new STExitPoll_TPollster(host, port);
        }
        
        return instance;
    }

    /**
     * The isOpen method is called by the pollster to check if the exit poll is open.
     * @return Boolean value that indicates if the exit poll is open.
     */
    public boolean isOpen(){
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.PS_IS_OPEN);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        /* 
            logic to handle the response from the server
        */

        com.close();

        return inMessage.getBooleanValue();
    };

    /**
     * The conductSurvey method is called by the pollster to conduct the survey to the seleceted voter.
     * @throws InterruptedException Exception that may be thrown if the thread is interrupted.
     */
    public void conductSurvey() throws InterruptedException{
        ClientCom com;                                                 
        Message outMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.CONDUCTSURVEY);
        
        com.writeObject(outMessage);

        /* 
            logic to handle the response from the server
        */

        com.close();
    };

    /**
     * The publishResults method is called by the pollster to publish the results of the survey.
     * @throws InterruptedException Exception that may be thrown if the thread is interrupted.
     */
    public void publishResults() throws InterruptedException{
        ClientCom com;                                                 
        Message outMessage; 

        com = new ClientCom(serverHost, serverPort);
        /* 
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.PUBLISHRESULTS);
        
        com.writeObject(outMessage);
        /* 
            logic to handle the response from the server
        */

        com.close();
    };
}
