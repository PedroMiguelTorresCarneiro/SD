// package serverSide.main;

// import commInfra.interfaces.Repository.IRepo_VotingBooth;
// import java.net.MalformedURLException;
// import java.rmi.Naming;
// import java.rmi.NotBoundException;
// import java.rmi.RemoteException;
// import serverSide.sharedRegions.EVotingBooth.MEvotingBooth;
// import serverSide.sharedRegions.RegisterRemoteObject;

// /**
//  * Servidor RMI da região partilhada EvotingBooth.
//  */
// public class SEvotingBooth {

//     public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
//         String host = "localhost";
//         int repoPort = 43000;

//         /* Acede ao repositório remoto via RMI */
//         IRepo_VotingBooth logs = (IRepo_VotingBooth)
//                 Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

//         /* Obtém a instância singleton do monitor */
//         MEvotingBooth booth = MEvotingBooth.getInstance(logs);

//         /* Exporta e regista no RMI registry */
//         RegisterRemoteObject.registerRemoteObject("EVotingBooth", booth);
//         System.out.println("Servidor RMI EvotingBooth pronto.");
//     }
// }

package serverSide.main;

import commInfra.interfaces.Repository.IRepo_VotingBooth;
import java.rmi.Naming;
import serverSide.sharedRegions.EVotingBooth.MEvotingBooth;
import serverSide.sharedRegions.RegisterRemoteObject;

/**
 * Servidor RMI da região partilhada EvotingBooth.
 */
public class SEvotingBooth {

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int registryPort = 1099;

            // Faz lookup ao repositório no registry central
            IRepo_VotingBooth logs = (IRepo_VotingBooth)
                    Naming.lookup("rmi://" + host + ":" + registryPort + "/Repository");

            // Instancia o monitor da EvotingBooth
            MEvotingBooth booth = MEvotingBooth.getInstance(logs);

            // Regista no RMI registry central
            RegisterRemoteObject.registerRemoteObject("EVotingBooth", booth);

            System.out.println("✅ EVotingBooth registado no RMI Registry central (porta " + registryPort + ").");

        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar EvotingBooth: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
