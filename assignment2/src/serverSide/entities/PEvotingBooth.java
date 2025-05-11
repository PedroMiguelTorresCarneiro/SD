package serverSide.entities;


import commInfra.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverSide.sharedRegions.EVotingBooth.IEVotingBooth;


public class PEvotingBooth implements Runnable{
    private final ServerCom sconi;
    private final IEVotingBooth votingBooth;
    
    
    public PEvotingBooth(ServerCom sconi, IEVotingBooth votingBooth) {
        this.sconi = sconi;
        this.votingBooth = votingBooth;
    }
    
    
   @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try{ 
            outMessage = votingBooth.processAndReply(inMessage);         // process it
        }
        catch (MessageException e)
        { System.out.println("Thread IDCheck: " + e.getMessage () + "!");
          System.out.println(e.getMessageVal ().toString ());
          System.exit (1);
        } catch (InterruptedException ex) {
            Logger.getLogger(PEvotingBooth.class.getName()).log(Level.SEVERE, null, ex);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}