/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverSide.main;

import java.net.SocketTimeoutException;

import clientSide.stubs.STRepo;
import commInfra.ServerCom;

import serverSide.entities.ExitPollClientProxy;
import serverSide.sharedRegions.ExitPoll;
import serverSide.sharedRegions.interfaces.ExitPollInterFace;

/**
 *
 * @author pedrocarneiro
 */
public class ServerExitPoll {
    /**
   *  Flag signaling the service is active.
   */

   public static boolean waitConnection;

  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - port nunber for listening to service requests
   *        args[1] - name of the platform where is located the server for the general repository
   *        args[2] - port nunber where the server for the general repository is listening to service requests
   */

   public static void main (String [] args)
   {
      ExitPoll exitPoll;                                              // barber shop (service to be rendered)
      ExitPollInterFace exitPInter;                              // interface to the barber shop
      STRepo reposStub;                                    // stub to the general repository
      ServerCom scon, sconi;                                         // communication channels
      int portNumb = -1;                                             // port number for listening to service requests
      String reposServerName;                                        // name of the platform where is located the server for the general repository
      int reposPortNumb = -1;                                        // port nunber where the server for the general repository is listening to service requests

      if (args.length != 3)
         { System.out.println("Wrong number of parameters!");
           System.exit (1);
         }
      try
      { portNumb = Integer.parseInt (args[0]);
      }
      catch (NumberFormatException e)
      { System.out.println("args[0] is not a number!");
        System.exit (1);
      }
      if ((portNumb < 4000) || (portNumb >= 65536))
         { System.out.println("args[0] is not a valid port number!");
           System.exit (1);
         }
      reposServerName = args[1];
      try
      { reposPortNumb = Integer.parseInt (args[2]);
      }
      catch (NumberFormatException e)
      { System.out.println("args[2] is not a number!");
        System.exit (1);
      }
      if ((reposPortNumb < 4000) || (reposPortNumb >= 65536))
         { System.out.println("args[2] is not a valid port number!");
           System.exit (1);
         }

     /* service is established */

      reposStub = new STRepo (reposServerName, reposPortNumb);       // communication to the general repository is instantiated
      exitPoll = new ExitPoll (reposStub);                                      // service is instantiated
      exitPInter = new ExitPollInterFace(exitPoll);                            // interface to the service is instantiated
      scon = new ServerCom (portNumb);                                         // listening channel at the public port is established
      scon.start ();
      System.out.println("Service is established!");
      System.out.println("Server is listening for service requests.");

     /* service request processing */

      ExitPollClientProxy cliProxy;                                // service provider agent

      waitConnection = true;
      while (waitConnection)
      { try
        { sconi = scon.accept ();                                    // enter listening procedure
          cliProxy = new ExitPollClientProxy(sconi, exitPInter);    // start a service provider agent to address
          cliProxy.start ();                                         //   the request of service
        }
        catch (SocketTimeoutException e) {}
      }
      scon.end ();                                                   // operations termination
      System.out.println("Server was shutdown.");
   }
    
}
