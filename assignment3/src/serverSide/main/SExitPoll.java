package serverSide.main;

import serverSide.sharedRegions.ExitPoll.MExitPoll;
import serverSide.sharedRegions.RegisterRemoteObject;
import commInfra.interfaces.Repository.IRepo_ExitPoll;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

/**
 * Servidor RMI da região partilhada ExitPoll.
 */
public class SExitPoll {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        /* Parâmetros de configuração */
        String host = "localhost";
        int repoPort = 43000;

        /* Acede ao repositório remoto via RMI */
        IRepo_ExitPoll logs = (IRepo_ExitPoll)
                Naming.lookup("rmi://" + host + ":" + repoPort + "/Repository");

        /* Obtém a instância singleton do monitor ExitPoll */
        MExitPoll exitPoll = MExitPoll.getInstance(logs);

        /* Regista a região partilhada no RMI registry */
        RegisterRemoteObject.registerRemoteObject("ExitPoll", exitPoll);

        System.out.println("Servidor RMI ExitPoll pronto.");
    }
}
