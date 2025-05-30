package serverSide.main;

import serverSide.sharedRegions.IDCheck.MIDCheck;
import serverSide.sharedRegions.RegisterRemoteObject;
import commInfra.interfaces.Repository.IRepo_IDChek;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

/**
 * Servidor RMI da região partilhada IDCheck.
 */
public class SIDCheck {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        /* Lê parâmetros do ficheiro .env ou define diretamente */
        String host = "localhost";
        int repoPort = 43000;

        /* Acede ao repositório remoto via RMI */
        IRepo_IDChek logs = (IRepo_IDChek)
                Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

        /* Obtém a instância singleton do monitor IDCheck */
        MIDCheck idCheck = MIDCheck.getInstance(logs);

        /* Regista no RMI registry */
        RegisterRemoteObject.registerRemoteObject("IDCheck", idCheck);

        System.out.println("Servidor RMI IDCheck pronto.");
    }
}
