package serverSide.sharedRegions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RegisterRemoteObject {

    public static void registerRemoteObject(String name, Remote obj) {
        try {
            // Tenta criar registo local se não existir
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("RMI registry criado na porta 1099.");
            } catch (RemoteException e) {
                System.out.println("RMI registry já em execução.");
            }

            // Exporta o objeto
            Remote stub = UnicastRemoteObject.exportObject(obj, 0);

            // Obtém referência ao registry e regista o objeto
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);

            System.out.println(name + " registado no RMI registry.");

        } catch (RemoteException e) {
            System.err.println("Erro ao registar objeto remoto: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
