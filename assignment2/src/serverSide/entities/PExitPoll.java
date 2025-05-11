package serverSide.entities;

import commInfra.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverSide.sharedRegions.ExitPoll.IExitPoll;

public class PExitPoll implements Runnable{
    private final ServerCom sconi;
    private final IExitPoll exitPoll;

    public PExitPoll(ServerCom sconi, IExitPoll exitPoll){
        this.sconi = sconi;
        this.exitPoll = exitPoll;
    }
    
    
    @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try{ 
            outMessage = exitPoll.processAndReply(inMessage);         // process it
        }
        catch (MessageException e)
        { System.out.println("Thread ExitPoll: " + e.getMessage () + "!");
          System.out.println(e.getMessageVal ().toString ());
          System.exit (1);
        } catch (InterruptedException ex) {
            Logger.getLogger(PExitPoll.class.getName()).log(Level.SEVERE, null, ex);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
