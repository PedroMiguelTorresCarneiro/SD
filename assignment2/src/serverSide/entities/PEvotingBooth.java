package serverSide.entities;


import commInfra.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverSide.sharedRegions.EVotingBooth.IEVotingBooth;

/**
 *
 * @author pedrocarneiro
 */
public class PEvotingBooth implements Runnable{
    private static PEvotingBooth instance = null;
    private final ServerCom sconi;
    private final IEVotingBooth votingBooth;
    private static int nProxy = 0;
    
    
    private PEvotingBooth(ServerCom sconi, IEVotingBooth votingBooth) {
        this.sconi = sconi;
        this.votingBooth = votingBooth;
    }
    
    public static PEvotingBooth getInstance(ServerCom sconi, IEVotingBooth votingBooth){
        if(instance == null){
            instance = new PEvotingBooth(sconi, votingBooth);
        }
        return instance;
    }
    
    private static int getProxyId(){
        Class<?> cl = null;                                            // representation of the PEvotingBooth object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.PEvotingBooth");
        }
        catch (ClassNotFoundException e)
        { System.out.println("Data type PEvotingBooth was not found!");
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
   public void run()
   {
      Message inMessage = null,                                      // service request
              outMessage = null;                                     // service reply

     /* service providing */

      inMessage = (Message) sconi.readObject ();                     // get service request
      try{ 
          outMessage = votingBooth.processAndReply(inMessage);         // process it
      }
      catch (MessageException e)
      { System.out.println("Thread VotingBooth: " + e.getMessage () + "!");
        System.out.println(e.getMessageVal ().toString ());
        System.exit (1);
      } catch (InterruptedException ex) {
            Logger.getLogger(PEvotingBooth.class.getName()).log(Level.SEVERE, null, ex);
        }
      sconi.writeObject (outMessage);                                // send service reply
      sconi.close ();                                                // close the communication channel
   }
}