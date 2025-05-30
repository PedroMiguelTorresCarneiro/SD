package serverSide.main;

import java.rmi.Naming;
import java.rmi.RemoteException;

import serverSide.sharedRegions.EVotingBooth.MEvotingBooth;
import serverSide.sharedRegions.RegisterRemoteObject;
import commInfra.interfaces.Repository.IRepo_VotingBooth;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

/**
 * Servidor RMI da região partilhada EvotingBooth.
 */
public class SEvotingBooth {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        String host = "localhost";
        int repoPort = 43000;

        /* Acede ao repositório remoto via RMI */
        IRepo_VotingBooth logs = (IRepo_VotingBooth)
                Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

        /* Obtém a instância singleton do monitor */
        MEvotingBooth booth = MEvotingBooth.getInstance(logs);

        /* Exporta e regista no RMI registry */
        RegisterRemoteObject.registerRemoteObject("EVotingBooth", booth);
        System.out.println("Servidor RMI EvotingBooth pronto.");
    }
}
