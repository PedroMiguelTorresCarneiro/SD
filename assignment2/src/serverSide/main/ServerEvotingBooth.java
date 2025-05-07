
import commInfra.Message;
import commInfra.RoleType;
import commInfra.ServerCom;
import serverSide.entities.EvotingBoothClientProxy;
import serverSide.sharedRegions.EVotingBooth.IEVotingBooth_TPollClerk;
import serverSide.sharedRegions.EVotingBooth.IEVotingBooth_TVoter;
import serverSide.sharedRegions.EVotingBooth.MEvotingBooth;
import java.net.*;


public class ServerEvotingBooth {

    /**
     * Variável de controlo para aceitar ou não novas ligações.
     */
    public static boolean waitConnection;

    /**
     *  Main method.
     *
     *  Starts the server for the EVotingBooth shared region.
     *
     *  @param args runtime arguments:
     *         args[0] - port number for listening to service requests
     *         args[1] - name of the platform where the General Repository server is located
     *         args[2] - port number where the General Repository server is listening
     */
    public static void main(String[] args) {

        int portNumb = -1;
        String reposServerName;
        int reposPortNumb = -1;

        // === Validar número de argumentos ===
        if (args.length != 3) {
            System.err.println("Wrong number of parameters!");
            System.exit(1);
        }

        // === Validar porto do EVotingBooth ===
        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("args[0] is not a number!");
            System.exit(1);
        }

        if (portNumb < 4000 || portNumb >= 65536) {
            System.err.println("args[0] is not a valid port number!");
            System.exit(1);
        }

        // === Validar hostname e porto do repositorio ===
        reposServerName = args[1];

        try {
            reposPortNumb = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("args[2] is not a number!");
            System.exit(1);
        }

        if (reposPortNumb < 4000 || reposPortNumb >= 65536) {
            System.err.println("args[2] is not a valid port number!");
            System.exit(1);
        }

        // === Inicializar monitor e interfaces ===
        waitConnection = true;
        Repo logs = new Repo(reposServerName, reposPortNumb);

        MEvotingBooth booth = new MEvotingBooth.getInstance(logs);
        IEVotingBooth_TVoter tvoterInterface = IEVotingBooth_TVoter.getInstance(booth);
        IEVotingBooth_TPollClerk tclerkInterface = IEVotingBooth_TPollClerk.getInstance(booth);

        ServerCom serverCom = new ServerCom(portNumb);
        serverCom.start();

        System.out.println("[EVotingBooth] Servidor iniciado no porto " + portNumb + ".");

        while (waitConnection) {
            try{
              ServerCom sconi = serverCom.accept();

              Message firstMessage = (Message) sconi.readObject();
              RoleType role = firstMessage.getRoleType();

              EvotingBoothClientProxy cliProxy = switch (role) {
                  case TVOTER -> new EvotingBoothClientProxy(sconi, tvoterInterface, null, firstMessage);
                  case TPOLLCLERK -> new EvotingBoothClientProxy(sconi, null, tclerkInterface, firstMessage);
                  default -> {
                      System.err.println("[EVotingBooth] Ligação com RoleType inválido: " + role);
                      sconi.close();
                      yield null;
                  }
              };

              if (cliProxy != null) cliProxy.start();
            } catch (SocketTimeoutException e) {}
        }

        serverCom.end();
        System.out.println("[EVotingBooth] Servidor encerrado.");
    }
}
