package serverSide.entities;

import commInfra.*;
import serverSide.sharedRegions.PollStation.IPollStation;

/**
 *
 * @author pedrocarneiro
 */
public class PPollStation implements Runnable {
    private static PPollStation instance = null;
    private final ServerCom sconi;
    private final IPollStation poll;
    private static int nProxy = 0;

    public PPollStation(ServerCom sconi, IPollStation poll){
        this.sconi = sconi;
        this.poll = poll;
    }
    
    public static PPollStation getInstance(ServerCom sconi, IPollStation poll){
        if(instance == null){
            instance = new PPollStation(sconi, poll);
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
        { System.out.println("Data type PPollStation was not found!");
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
