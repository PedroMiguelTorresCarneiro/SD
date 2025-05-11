package serverSide.entities;


import commInfra.*;
import serverSide.sharedRegions.MainGui.ImainGUI;


/**
 *
 * @author pedrocarneiro
 */
public class PmainGUI implements Runnable {
    private static PmainGUI instance = null;
    private final ServerCom sconi;
    private final ImainGUI gui;
    private static int nProxy = 0;

    public PmainGUI(ServerCom sconi, ImainGUI gui){
        this.sconi = sconi;
        this.gui = gui;
    }
    
    public static PmainGUI getInstance(ServerCom sconi, ImainGUI gui){
        if(instance == null){
            instance = new PmainGUI(sconi, gui);
        }
        return instance;
    }
    
    private static int getProxyId(){
        Class<?> cl = null;                                            // representation of the PEvotingBooth object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.PIDCheck");
        }
        catch (ClassNotFoundException e)
        { System.out.println("Data type PmainGUI was not found!");
          e.printStackTrace ();
          System.exit (1);
        }
        synchronized (cl)
        { proxyId = nProxy;
          nProxy += 1;
        }
        return proxyId;
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
