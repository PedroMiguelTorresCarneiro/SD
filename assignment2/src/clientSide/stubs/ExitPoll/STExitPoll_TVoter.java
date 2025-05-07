package clientSide.stubs.ExitPoll;

import clientSide.stubs.Stub;
import commInfra.MessageType;
import commInfra.RoleType;

/**
 * The IExitPoll_TVoter interface contains the methods that the exit poll shared region
 * should implement to interact with the voter threads.
 */
public class STExitPoll_TVoter extends Stub{
    private static STExitPoll_TVoter instance = null;
  
    private STExitPoll_TVoter(String host, int port){
        super(host, port);
    }

    public static STExitPoll_TVoter getInstance(String host, int port){
        if (instance == null) {
            instance = new STExitPoll_TVoter(host, port);
        }
        
        return instance;
    }

    /**
     * The choosen method is called by the voter to see if he was choosed for the survey.
     * @return true if the voter was choosen for the survey, false otherwise.
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean choosen() throws InterruptedException{
        return boolComm(MessageType.VOTER_CHOOSEN, RoleType.VOTER);
    }

    /**
     * The callForSurvey method is called by the voter to vote in the survey.
     * @param vote The vote of the voter
     * @param voterId The ID of the voter
     * @throws InterruptedException if the thread is interrupted
     */
    public void callForSurvey(Character vote, String voterId) throws InterruptedException{
        sendMessage(MessageType.CALLFORSURVEY, RoleType.VOTER, voterId);
    }
    
    /**
     * The isOpen method is called by the voter to see if the exit poll is open.
     * @return true if the exit poll is open, false otherwise.
     */
    public boolean isOpen(){
        return boolComm(MessageType.EP_ISOPEN, RoleType.VOTER);
    }
}
