// package serverSide.main;

// import commInfra.interfaces.Repository.IRepo_PollStation;
// import java.net.MalformedURLException;
// import java.rmi.Naming;
// import java.rmi.NotBoundException;
// import java.rmi.RemoteException;
// import serverSide.sharedRegions.PollStation.MPollStation;
// import serverSide.sharedRegions.RegisterRemoteObject;

// /**
//  * Servidor RMI da região partilhada PollStation.
//  */
// public class SPollStation {

//     public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
//         /* Lê parâmetros do ficheiro .env ou define diretamente */
//         String host = "localhost";
//         int repoPort = 43000;
//         int maxInside = 5; // ou: EnvReader.getInt("MAX_INSIDE");

//         /* Acede ao repositório remoto via RMI */
//         IRepo_PollStation logs = (IRepo_PollStation)
//                 Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

//         /* Instancia o monitor */
//         MPollStation pollStation = MPollStation.getInstance(maxInside, logs);

//         /* Regista no RMI Registry */
//         RegisterRemoteObject.registerRemoteObject("PollStation", pollStation);

//         System.out.println("Servidor RMI PollStation pronto.");
//     }
// }

package serverSide.main;

import commInfra.interfaces.Repository.IRepo_PollStation;
import java.rmi.Naming;
import serverSide.sharedRegions.PollStation.MPollStation;
import serverSide.sharedRegions.RegisterRemoteObject;

/**
 * Servidor RMI da região partilhada PollStation.
 */
public class SPollStation {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int registryPort = 1099;
            int maxInside = 5; // ou ler de .env com EnvReader.getInt("MAX_INSIDE")

            // Faz lookup ao repositório no registry central
            IRepo_PollStation logs = (IRepo_PollStation)
                    Naming.lookup("rmi://" + host + ":" + registryPort + "/Repository");

            // Instancia o monitor
            MPollStation pollStation = MPollStation.getInstance(maxInside, logs);

            // Regista no RMI registry central
            RegisterRemoteObject.registerRemoteObject("PollStation", pollStation);

            System.out.println("✅ PollStation registado no RMI Registry central (porta " + registryPort + ").");

        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar PollStation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
