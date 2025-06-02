// package serverSide.main;

// import commInfra.interfaces.MainGUI.IMainGUI_ALL;
// import java.rmi.Naming;
// import java.rmi.registry.LocateRegistry;
// import serverSide.sharedRegions.RegisterRemoteObject;
// import serverSide.sharedRegions.Repository.MRepo;

// /**
//  * Servidor RMI da região partilhada Repository com GUI e configuração via .env.
//  */
// public class SRepository {

//     private static boolean isRunning = false;
    
//     /**
//      * Lança o backend com ligação à mainGUI remota via RMI.
//      * @param votesToEnd
//      * @param numVoters
//      * @param maxInside
//      */
//     public static void launchBackend(int votesToEnd, int numVoters, int maxInside) {
//         if (isRunning) return;
//         isRunning = true;

//         try {
//             int repoPort = 43000;

//             String guiHost = "localhost";
//             int guiPort = 47000;

//             // Cria o registry local para o Repository
//             LocateRegistry.createRegistry(repoPort);

//             // Faz lookup à GUI remota
//             IMainGUI_ALL gui = (IMainGUI_ALL)
//                     Naming.lookup("rmi://" + guiHost + ":" + guiPort + "/MainGUI");

//             // Instancia o monitor e associa GUI
//             MRepo.resetInstance();
//             MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);
//             mrepo.setGui(gui);

//             // Regista no RMI Registry
//             RegisterRemoteObject.registerRemoteObject("Repository", mrepo);

//             System.out.println("✅ Repository RMI registado na porta " + repoPort);

//         } catch (Exception e) {
//             System.err.println("Erro ao iniciar Repository com GUI remota: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         }
//     }

//     /**
//      * Lança o servidor Repository sem GUI (modo clássico).
//      */
//     public static void main(String[] args) {
//         if (isRunning) return;
//         isRunning = true;

//         try {
//             if (args.length != 3) {
//                 System.err.println("Uso: java SRepository <votesToEnd> <numVoters> <maxInside>");
//                 System.exit(1);
//             }

//             int votesToEnd = Integer.parseInt(args[0]);
//             int numVoters = Integer.parseInt(args[1]);
//             int maxInside = Integer.parseInt(args[2]);
//             int repoPort = 43000;

//             LocateRegistry.createRegistry(repoPort);
            

//             MRepo.resetInstance();
//             MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);

//             RegisterRemoteObject.registerRemoteObject("Repository", mrepo);
//             System.out.println("✅ Repository RMI pronto na porta " + repoPort);

//         } catch (Exception e) {
//             System.err.println("Erro ao iniciar Repository: " + e.getMessage());
//             e.printStackTrace();
//             System.exit(1);
//         }
//     }

// }

package serverSide.main;

import commInfra.interfaces.MainGUI.IMainGUI_ALL;
import java.rmi.Naming;
import serverSide.sharedRegions.RegisterRemoteObject;
import serverSide.sharedRegions.Repository.MRepo;

/**
 * Servidor RMI da região partilhada Repository com GUI e configuração via .env.
 */
public class SRepository {

    private static boolean isRunning = false;

    /**
     * Lança o backend com ligação à mainGUI remota via RMI.
     * @param votesToEnd número de votos para terminar a eleição
     * @param numVoters número total de votantes
     * @param maxInside máximo de votantes simultâneos
     */
    public static void launchBackend(int votesToEnd, int numVoters, int maxInside) {
        if (isRunning) return;
        isRunning = true;

        try {
            String guiHost = "localhost";
            int registryPort = 1099;

            // Faz lookup à GUI remota no RMI registry central
            IMainGUI_ALL gui = (IMainGUI_ALL)
                    Naming.lookup("rmi://" + guiHost + ":" + registryPort + "/MainGUI");

            // Instancia o monitor e associa GUI
            MRepo.resetInstance();
            MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);
            mrepo.setGui(gui);

            // Regista o Repository no RMI Registry central
            RegisterRemoteObject.registerRemoteObject("Repository", mrepo);

            System.out.println("✅ Repository RMI registado com GUI no RMI Registry central (porta " + registryPort + ")");

        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar Repository com GUI remota: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Lança o servidor Repository sem GUI (modo clássico).
     */
    public static void main(String[] args) {
        if (isRunning) return;
        isRunning = true;

        try {
            if (args.length != 3) {
                System.err.println("Uso: java SRepository <votesToEnd> <numVoters> <maxInside>");
                System.exit(1);
            }

            int votesToEnd = Integer.parseInt(args[0]);
            int numVoters = Integer.parseInt(args[1]);
            int maxInside = Integer.parseInt(args[2]);
            String guiHost = "localhost";
            int registryPort = 1099;

            // Faz lookup à GUI remota no RMI registry central
            IMainGUI_ALL gui = (IMainGUI_ALL)
                    Naming.lookup("rmi://" + guiHost + ":" + registryPort + "/MainGUI");

            // Instancia o monitor sem GUI
            MRepo.resetInstance();
            MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);
            mrepo.setGui(gui);

            // Regista no RMI Registry central
            RegisterRemoteObject.registerRemoteObject("Repository", mrepo);

            System.out.println("✅ Repository RMI pronto (modo clássico) no RMI Registry central (porta " + registryPort + ")");

        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar Repository: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

