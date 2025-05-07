package clientSide.stubs;

import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;

public class STRepo extends Stub {

    public STRepo(String host, int port) {
        super(host, port);
    }


    /**
   *   Operation initialization of the simulation.
   *
   *     @param fileName logging file name
   *     @param nIter number of iterations of the customer life cycle
   */

   public void initSimul (String fileName, int nIter)
   {
      ClientCom com;                                                 // communication channel
      Message outMessage,                                            // outgoing message
              inMessage;                                             // incoming message

      com = new ClientCom (serverHostName, serverPortNumb);
      while (!com.open ())
      { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
      }
      outMessage =Message.getInstance(MessageType.SETNFIC, fileName, nIter);
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();
      if (inMessage.getMsgType() != MessageType.NFICDONE)
         { System.out.println ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
           System.out.println (inMessage.toString ());
           System.exit (1);
         }
      com.close ();
   }


      /**
   *   Operation server shutdown.
   *
   *   New operation.
   */

   public void shutdown ()
   {
      ClientCom com;                                                 // communication channel
      Message outMessage,                                            // outgoing message
              inMessage;                                             // incoming message

      com = new ClientCom (serverHostName, serverPortNumb);
      while (!com.open ())
      { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
      }
      outMessage = Message.getInstance(MessageType.SHUT);
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();
      if (inMessage.getMsgType() != MessageType.SHUTDONE)
         { System.out.println ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
           System.out.println (inMessage.toString ());
           System.exit (1);
         }
      com.close ();
   }
}
