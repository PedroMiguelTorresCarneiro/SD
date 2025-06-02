// package serverSide.main;

// import java.rmi.registry.LocateRegistry;
// import javax.swing.SwingUtilities;
// import serverSide.sharedRegions.MainGui.mainGUI;
// import serverSide.sharedRegions.RegisterRemoteObject;

// /**
//  * Servidor RMI da mainGUI.
//  */
// public class SmainGUI {

//     /**
//      * Porto onde o servidor da GUI escuta via RMI.
//      */
//     private static final int GUI_PORT = 47000;

//     public static void main(String[] args) {
//         System.out.println("🎛️  Servidor RMI MainGUI a escutar no porto " + GUI_PORT + "...");

//         try {
//             // Iniciar RMI registry na porta especificada
//             LocateRegistry.createRegistry(GUI_PORT);

//             // Criar a interface gráfica de forma síncrona
//             final mainGUI[] gui = new mainGUI[1];
//             SwingUtilities.invokeAndWait(() -> {
//                 gui[0] = new mainGUI();
//                 gui[0].setVisible(true);
//             });

//             // Registar GUI como objeto remoto no RMI Registry
//             RegisterRemoteObject.registerRemoteObject("MainGUI", gui[0]);

//             System.out.println("✅ mainGUI registada com sucesso no RMI Registry.");

//         } catch (Exception e) {
//             System.err.println("Erro ao iniciar a mainGUI: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
// }

package serverSide.main;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.SwingUtilities;
import serverSide.sharedRegions.MainGui.mainGUI;

public class SmainGUI {

    public static void main(String[] args) {
        try {
            System.out.println("🎛️ A iniciar o RMI Registry na porta 1099...");
            LocateRegistry.createRegistry(1099); // ✅ Só um registry!

            final mainGUI[] gui = new mainGUI[1];
            SwingUtilities.invokeAndWait(() -> {
                gui[0] = new mainGUI();
                gui[0].setVisible(true);
            });

            Remote stub = UnicastRemoteObject.exportObject(gui[0], 0);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            registry.rebind("MainGUI", stub);

            System.out.println("✅ GUI registada com sucesso no registry!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao registar GUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
