package serverSide.entities;

import commInfra.*;
import serverSide.sharedRegions.PollStation.IPollStation;

/**
 *
 * @author pedrocarneiro
 */
public class PPollStation implements Runnable {
    private final ServerCom sconi;
    private final IPollStation poll;

    public PPollStation(ServerCom sconi, IPollStation poll){
        this.sconi = sconi;
        this.poll = poll;
    }
    
    
    @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try{ 
            outMessage = poll.processAndReply(inMessage);         // process it
        }
        catch (MessageException e)
        { System.out.println("Thread IPollStation: " + e.getMessage () + "!");
          System.out.println(e.getMessageVal ().toString ());
          System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
