package serverSide.main;

import serverSide.sharedRegions.MainGui.mainGUI;
import serverSide.sharedRegions.RegisterRemoteObject;

import javax.swing.SwingUtilities;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

/**
 * Servidor RMI da mainGUI.
 */
public class SmainGUI {

    /**
     * Porto onde o servidor da GUI escuta via RMI.
     */
    private static final int GUI_PORT = 47000;

    public static void main(String[] args) {
        System.out.println("🎛️  Servidor RMI MainGUI a escutar no porto " + GUI_PORT + "...");

        try {
            // Iniciar RMI registry na porta especificada
            LocateRegistry.createRegistry(GUI_PORT);

            // Criar a interface gráfica de forma síncrona
            final mainGUI[] gui = new mainGUI[1];
            SwingUtilities.invokeAndWait(() -> {
                gui[0] = new mainGUI();
                gui[0].setVisible(true);
            });

            // Registar GUI como objeto remoto no RMI Registry
            RegisterRemoteObject.registerRemoteObject("MainGUI", gui[0]);

            System.out.println("✅ mainGUI registada com sucesso no RMI Registry.");

        } catch (Exception e) {
            System.err.println("Erro ao iniciar a mainGUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
