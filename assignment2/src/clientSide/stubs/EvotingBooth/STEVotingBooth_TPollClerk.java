package clientSide.stubs.EvotingBooth;
import clientSide.stubs.Stub;
import commInfra.MessageType;


/**
 * The IEVotingBooth_TPollClerk interface contains the methods that the evoting booth shared region
 * should implement to interact with the poll clerk thread.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class STEVotingBooth_TPollClerk extends Stub {
    private static STEVotingBooth_TPollClerk instance = null;

    private STEVotingBooth_TPollClerk(String host, int port){
        super(host, port);
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
        sendMessage(MessageType.GATHERING_VOTES);
    }
    
    /**
     * The publishElectionResults method is called by the poll clerk to publish the election results.
     */
    public void publishElectionResults(){
        sendMessage(MessageType.PUBLISH_ELECTION_RESULTS);
    }
    
    /**
     * The getVotesCount method is called by the poll clerk to get the number of voters.
     * @return the number of voters that have voted.
     */
    public int getVotesCount(){
        return intComm(MessageType.GET_VOTES_COUNT);
    }
}
