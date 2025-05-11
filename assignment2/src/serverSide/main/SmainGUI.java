package serverSide.main;

import commInfra.ServerCom;
import java.net.SocketTimeoutException;
import javax.swing.SwingUtilities;
import serverSide.entities.PmainGUI;
import serverSide.sharedRegions.MainGui.ImainGUI;
import serverSide.sharedRegions.MainGui.mainGUI;

public class SmainGUI {
    /**
     * Porto onde o servidor da GUI escuta.
     */
    private static final int GUI_PORT = 47000;

    public static void main(String[] args) throws SocketTimeoutException {
        System.out.println("🎛️  Servidor MainGUI a escutar no porto " + GUI_PORT + "...");

        final mainGUI[] gui = new mainGUI[1];

        try {
            SwingUtilities.invokeAndWait(() -> {
                gui[0] = new mainGUI();
                gui[0].setVisible(true);
            });

            //SRepository.launchBackend(gui[0]);

        } catch (Exception e) {
            System.err.println("Erro ao iniciar a mainGUI: " + e.getMessage());
            return;
        }

        // Interface TCP que chama métodos da instância real da GUI
        ImainGUI interfaceGUI = new ImainGUI(gui[0]);

        ServerCom server = new ServerCom(GUI_PORT);
        server.start();

        while (true) {
            ServerCom sconi = server.accept();
            PmainGUI clientProxy = new PmainGUI(sconi, interfaceGUI);
            new Thread(clientProxy).start();
        }
    }
}
