import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

public class SRegisterRemoteObject {
    public static void main(String[] args) {
        int port = 1099;
        try {
            LocateRegistry.createRegistry(port);
            System.out.println("RMI registry iniciado na porta " + port);
        } catch (RemoteException e) {
            System.out.println("RMI registry já está ativo.");
        }
    }
}
