package serverSide.main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import serverSide.sharedRegions.RegisterRemoteObject;
import serverSide.sharedRegions.Repository.MRepo;
import commInfra.interfaces.Repository.IRepo;
import utils.EnvReader;

/**
 * Servidor RMI da região partilhada Repository com GUI e configuração via .env.
 */
public class SRepository {

    private static boolean isRunning = false;

    /**
     * Lança o backend com integração à GUI (mainGUI).
     */
    public static void launchBackend(serverSide.sharedRegions.MainGui.mainGUI gui) {
        if (isRunning) return;
        isRunning = true;

        try {
            int votesToEnd = 20;
            int numVoters = 10;
            int maxInside = 5;
            int repoPort = 43000;

            // Cria o registry RMI localmente
            LocateRegistry.createRegistry(repoPort);

            // Instancia monitor
            MRepo.resetInstance(); // limpa anterior se houver
            MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);
            mrepo.setGui(gui); // conecta a GUI, se necessário

            // Regista no RMI registry
            RegisterRemoteObject.registerRemoteObject("Repository", mrepo);

            System.out.println("✅ Repository RMI registado na porta " + repoPort);

        } catch (Exception e) {
            System.err.println("Erro ao iniciar Repository com GUI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Lança o servidor Repository sem GUI (modo clássico).
     */
    public static void main(String[] args) {
        try {
            int votesToEnd = EnvReader.getInt("VOTES_TO_END");
            int numVoters = EnvReader.getInt("NUM_VOTERS");
            int maxInside = EnvReader.getInt("MAX_INSIDE");
            int repoPort = EnvReader.getInt("REPOSITORY_PORT");

            // Cria o registry RMI localmente
            LocateRegistry.createRegistry(repoPort);

            // Instancia monitor
            MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);

            // Regista no RMI registry
            RegisterRemoteObject.registerRemoteObject("Repository", mrepo);

            System.out.println("✅ Repository RMI pronto na porta " + repoPort);

        } catch (RemoteException e) {
            System.err.println("Erro RMI no Repository: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
