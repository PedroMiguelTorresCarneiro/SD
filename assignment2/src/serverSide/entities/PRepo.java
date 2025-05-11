package serverSide.entities;

import commInfra.*;
import serverSide.sharedRegions.Repository.IRepo;

public class PRepo implements Runnable {
    private final ServerCom sconi;
    private final IRepo repo;

    public PRepo(ServerCom sconi, IRepo repo){
        this.sconi = sconi;
        this.repo = repo;
    }
     
    
    @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        //System.out.println("Incoming message: " + inMessage.toString());
        try{ 
            outMessage = repo.processAndReply(inMessage);         // process it
        }
        catch (MessageException e)
        { System.out.println("Thread IRepo: " + e.getMessage () + "!");
          System.out.println(e.getMessageVal ().toString ());
          System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
