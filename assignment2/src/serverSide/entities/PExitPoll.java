package serverSide.entities;

import commInfra.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverSide.sharedRegions.ExitPoll.IExitPoll;

public class PExitPoll implements Runnable{
    private static PExitPoll instance = null;
    private final ServerCom sconi;
    private final IExitPoll exitPoll;
    private static int nProxy = 0;

    public PExitPoll(ServerCom sconi, IExitPoll exitPoll){
        this.sconi = sconi;
        this.exitPoll = exitPoll;
    }
    
    public static PExitPoll getInstance(ServerCom sconi, IExitPoll exitPoll){
        if(instance == null){
            instance = new PExitPoll(sconi, exitPoll);
        }
        return instance;
    }
    
    private static int getProxyId(){
        Class<?> cl = null;                                            // representation of the PEvotingBooth object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.PExitPoll");
        }
        catch (ClassNotFoundException e)
        { System.out.println("Data type PExitPoll was not found!");
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
