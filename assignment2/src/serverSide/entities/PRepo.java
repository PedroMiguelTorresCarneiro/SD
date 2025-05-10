package serverSide.entities;

import commInfra.*;
import serverSide.sharedRegions.Repository.IRepo;

public class PRepo implements Runnable {
    private static PRepo instance = null;
    private final ServerCom sconi;
    private final IRepo repo;
    private static int nProxy = 0;

    private PRepo(ServerCom sconi, IRepo repo){
        this.sconi = sconi;
        this.repo = repo;
    }
    
    public static PRepo getInstance(ServerCom sconi, IRepo repo){
        if(instance == null){
            instance = new PRepo(sconi, repo);
        }
        return instance;
    }
    
    private static int getProxyId(){
        Class<?> cl = null;                                            // representation of the PEvotingBooth object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.PRepo");
        }
        catch (ClassNotFoundException e)
        { System.out.println("Data type PRepo was not found!");
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
