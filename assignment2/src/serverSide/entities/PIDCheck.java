package serverSide.entities;


import commInfra.*;
import serverSide.sharedRegions.IDCheck.IIDCheck;


/**
 *
 * @author pedrocarneiro
 */
public class PIDCheck implements Runnable {
    private final ServerCom sconi;
    private final IIDCheck idCheck;

    public PIDCheck(ServerCom sconi, IIDCheck idCheck){
        this.sconi = sconi;
        this.idCheck = idCheck;
    }

    
    @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try{ 
            outMessage = idCheck.processAndReply(inMessage);         // process it
        }
        catch (MessageException e)
        { System.out.println("Thread IDCheck: " + e.getMessage () + "!");
          System.out.println(e.getMessageVal ().toString ());
          System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
