package serverSide.entities;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;

public class Proxy extends Thread {

    private static int nProxy = 0; // Number of proxies created

    protected ServerCom sconi; // Communication channel with the client 

    public Proxy(String proxyName, ServerCom sconi)  {
        super(proxyName + "_" + Proxy.getProxyId()); 
        this.sconi = sconi; 
    }


       /**
   *  Generation of the instantiation identifier.
   *
   *     @return instantiation identifier
   */

   private static int getProxyId ()
   {
      Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
      int proxyId;                                                   // instantiation identifier

      try
      { cl = Class.forName ("serverSide.entities.ProxyExitPoll");
      }
      catch (ClassNotFoundException e)
      { System.out.println ("Data type ProxyExitPoll was not found!");
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
     public void run ()
    {
        Message inMessage = null;                                    // message from the client
        Message outMessage = null;                                   // message to be sent to the client

        inMessage = (Message) sconi.readObject();                    // get the message from the socket

        try {
            outMessage = sharedRegionInterface.processAndReply(inMessage);   // process it and get the reply
        } catch (MessageException e) {
            System.out.println("Error processing message: " + e.getMessage());
        }

        sconi.writeObject(outMessage);                               // send the reply to the client

        sconi.close();                                               // close the socket
    }

}
