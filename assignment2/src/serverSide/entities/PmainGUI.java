package serverSide.entities;


import commInfra.*;
import serverSide.sharedRegions.MainGui.ImainGUI;


/**
 *
 * @author pedrocarneiro
 */
public class PmainGUI implements Runnable {
    private final ServerCom sconi;
    private final ImainGUI gui;

    public PmainGUI(ServerCom sconi, ImainGUI gui){
        this.sconi = sconi;
        this.gui = gui;
    }
    
    
    @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try{ 
            outMessage = gui.processAndReply(inMessage);         // process it
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
