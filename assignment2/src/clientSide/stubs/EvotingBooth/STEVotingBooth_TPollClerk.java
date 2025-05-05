package clientSide.stubs.EvotingBooth;

/**
 * The IEVotingBooth_TPollClerk interface contains the methods that the evoting booth shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class STEVotingBooth_TPollClerk {
    private static STEVotingBooth_TPollClerk instance = null;
    private final String serverHost;
    private final int serverPort;

    private STEVotingBooth_TPollClerk(String host, int port){
        this.serverHost = host;
        this.serverPort = port;
    }

    public STEVotingBooth_TPollClerk getInstance(String host, int port){
        if (instance == null) {
            instance = new STEVotingBooth_TPollClerk(host, port);
        }
        return instance;
    }
    
    /**
     * The gathering method is called by the poll clerk to gather all the votes.
     * @throws InterruptedException if the thread is interrupted.
     */
    public void gathering() throws InterruptedException{
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHost, serverPort);
        /*
            logic to handle the connection to the server
            and the response from the server
        */

        outMessage = new Message(MessageType.GATHERING, null);
        
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        /*
            logic to handle the response from the server
        */

        com.close();
    };
    
    /**
     * The publishElectionResults method is called by the poll clerk to publish the election results.
     */
    public void publishElectionResults(){

    };
    
    /**
     * The getVotesCount method is called by the poll clerk to get the number of voters.
     * @return the number of voters that have voted.
     */
    public int getVotesCount(){

    };
}
