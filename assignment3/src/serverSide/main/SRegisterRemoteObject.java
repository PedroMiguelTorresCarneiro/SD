// package serverSide.main;

// import java.rmi.*;
// import java.rmi.registry.*;

// public class SRegisterRemoteObject {
//     public static void main(String[] args) {
//         int port = 1900;
//         try {
//             LocateRegistry.createRegistry(port);
//             System.out.println("RMI registry iniciado na porta " + port);
//         } catch (RemoteException e) {
//             System.out.println("RMI registry já está ativo.");
//         }
//     }
// }

package serverSide.main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class SRegisterRemoteObject {
    public static void main(String[] args) {
        int port = 1099;

        try {
            LocateRegistry.createRegistry(port);
            System.out.println("✅ RMI registry iniciado na porta " + port);
            System.out.println("🔒 A aguardar... (CTRL+C para terminar)");

            // Mantém o processo vivo
            Object lock = new Object();
            synchronized (lock) {
                lock.wait(); // Aguarda indefinidamente
            }

        } catch (RemoteException e) {
            System.out.println("⚠️ RMI registry já está ativo na porta " + port);
        } catch (InterruptedException ie) {
            System.out.println("🛑 Execução interrompida.");
        }
    }
}
