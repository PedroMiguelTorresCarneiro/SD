package serverSide.main;

import serverSide.sharedRegions.PollStation.MPollStation;
import serverSide.sharedRegions.RegisterRemoteObject;
import commInfra.interfaces.Repository.IRepo_PollStation;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

/**
 * Servidor RMI da região partilhada PollStation.
 */
public class SPollStation {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        /* Lê parâmetros do ficheiro .env ou define diretamente */
        String host = "localhost";
        int repoPort = 43000;
        int maxInside = 5; // ou: EnvReader.getInt("MAX_INSIDE");

        /* Acede ao repositório remoto via RMI */
        IRepo_PollStation logs = (IRepo_PollStation)
                Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

        /* Instancia o monitor */
        MPollStation pollStation = MPollStation.getInstance(maxInside, logs);

        /* Regista no RMI Registry */
        RegisterRemoteObject.registerRemoteObject("PollStation", pollStation);

        System.out.println("Servidor RMI PollStation pronto.");
    }
}
