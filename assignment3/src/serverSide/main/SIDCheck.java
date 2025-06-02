// package serverSide.main;

// import commInfra.interfaces.Repository.IRepo_IDChek;
// import java.net.MalformedURLException;
// import java.rmi.Naming;
// import java.rmi.NotBoundException;
// import java.rmi.RemoteException;
// import serverSide.sharedRegions.IDCheck.MIDCheck;
// import serverSide.sharedRegions.RegisterRemoteObject;

// /**
//  * Servidor RMI da região partilhada IDCheck.
//  */
// public class SIDCheck {

//     public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
//         /* Lê parâmetros do ficheiro .env ou define diretamente */
//         String host = "localhost";
//         int repoPort = 43000;

//         /* Acede ao repositório remoto via RMI */
//         IRepo_IDChek logs = (IRepo_IDChek)
//                 Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

//         /* Obtém a instância singleton do monitor IDCheck */
//         MIDCheck idCheck = MIDCheck.getInstance(logs);

//         /* Regista no RMI registry */
//         RegisterRemoteObject.registerRemoteObject("IDCheck", idCheck);

//         System.out.println("Servidor RMI IDCheck pronto.");
//     }
// }

package serverSide.main;

import commInfra.interfaces.Repository.IRepo_IDChek;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import serverSide.sharedRegions.IDCheck.MIDCheck;
import serverSide.sharedRegions.RegisterRemoteObject;

public class SIDCheck {

    public static void main(String[] args) {
        String host = "localhost";
        int registryPort = 1099;

        try {
            // Conecta ao RMI Registry principal
            Registry registry = LocateRegistry.getRegistry(host, registryPort);

            // Obtém stub remoto do repositório previamente registado
            IRepo_IDChek logs = (IRepo_IDChek) registry.lookup("Repository");

            // Cria o monitor partilhado IDCheck
            MIDCheck idCheck = MIDCheck.getInstance(logs);

            // Regista o IDCheck no registry com nome "IDCheck"
            RegisterRemoteObject.registerRemoteObject("IDCheck", idCheck);

            System.out.println("✅ IDCheck registado com sucesso no RMI Registry.");

        } catch (RemoteException | NotBoundException e) {
            System.err.println("❌ Erro ao iniciar o SIDCheck: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
