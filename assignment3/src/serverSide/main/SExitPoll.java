// package serverSide.main;

// import commInfra.interfaces.Repository.IRepo_ExitPoll;
// import java.net.MalformedURLException;
// import java.rmi.Naming;
// import java.rmi.NotBoundException;
// import java.rmi.RemoteException;
// import serverSide.sharedRegions.ExitPoll.MExitPoll;
// import serverSide.sharedRegions.RegisterRemoteObject;

// /**
//  * Servidor RMI da região partilhada ExitPoll.
//  */
// public class SExitPoll {

//     public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
//         /* Parâmetros de configuração */
//         String host = "localhost";
//         int repoPort = 43000;

//         /* Acede ao repositório remoto via RMI */
//         IRepo_ExitPoll logs = (IRepo_ExitPoll)
//                 Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

//         /* Obtém a instância singleton do monitor ExitPoll */
//         MExitPoll exitPoll = MExitPoll.getInstance(logs);

//         /* Regista a região partilhada no RMI registry */
//         RegisterRemoteObject.registerRemoteObject("ExitPoll", exitPoll);

//         System.out.println("Servidor RMI ExitPoll pronto.");
//     }
// }

package serverSide.main;

import commInfra.interfaces.Repository.IRepo_ExitPoll;
import java.rmi.Naming;
import serverSide.sharedRegions.ExitPoll.MExitPoll;
import serverSide.sharedRegions.RegisterRemoteObject;

/**
 * Servidor RMI da região partilhada ExitPoll.
 */
public class SExitPoll {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int registryPort = 1099;

            // Faz lookup ao repositório no RMI Registry central
            IRepo_ExitPoll logs = (IRepo_ExitPoll)
                    Naming.lookup("rmi://" + host + ":" + registryPort + "/Repository");

            // Instancia o monitor ExitPoll com referência ao repositório
            MExitPoll exitPoll = MExitPoll.getInstance(logs);

            // Regista o monitor no RMI Registry central
            RegisterRemoteObject.registerRemoteObject("ExitPoll", exitPoll);

            System.out.println("✅ ExitPoll registado com sucesso no RMI Registry central (porta " + registryPort + ").");

        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar ExitPoll: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
