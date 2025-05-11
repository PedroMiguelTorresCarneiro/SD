package serverSide.main;

import commInfra.ServerCom;
import java.net.SocketTimeoutException;
import serverSide.entities.PRepo;
import serverSide.sharedRegions.MainGui.mainGUI;
import serverSide.sharedRegions.Repository.IRepo;
import serverSide.sharedRegions.Repository.MRepo;
import utils.EnvReader;

/**
 * Servidor da região partilhada Repository com GUI e configuração via .env.
 * 
 */
public class SRepository {

    /**
     * Flag que sinaliza se o serviço está ativo.
     */
    public static boolean waitConnection;
    private static ServerCom server;
    private static boolean isRunning = false;
    
    
    public static void launchBackend(mainGUI gui) throws SocketTimeoutException {
        if (isRunning) return; // evita múltiplos arranques
        isRunning = true;

        int votesToEnd = EnvReader.getInt("VOTES_TO_END");
        int numVoters = EnvReader.getInt("NUM_VOTERS");
        int maxInside = EnvReader.getInt("MAX_INSIDE");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");

        MRepo.resetInstance(); // novo monitor
        MRepo mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);

        IRepo repoInterface = IRepo.getInstance(mrepo);

        server = new ServerCom(repoPort);
        server.start();

        System.out.println("✅ ServerCom arrancado no porto " + repoPort);

        // 💡 A thread fica viva indefinidamente
        Thread acceptThread = new Thread(() -> {
            try {
                while (mrepo.isRunning()) {
                    ServerCom sconi = server.accept();
                    if (sconi != null)
                        new Thread(new PRepo(sconi, repoInterface)).start();
                }
            } catch (Exception e) {
                System.out.println("🛑 ServerCom terminado: " + e.getMessage());
            } finally {
                server.end();
                isRunning = false;
                System.out.println("🛑 SRepository terminou com sucesso.");
            }
        });
        acceptThread.start();
    }


    public static void main(String[] args) throws SocketTimeoutException {
        ServerCom server = null, sconi;
        MRepo mrepo;
        IRepo repoInterface;
        PRepo proxy;

        //Lê parâmetros do ficheiro .env
        int votesToEnd = EnvReader.getInt("VOTES_TO_END");
        int numVoters = EnvReader.getInt("NUM_VOTERS");
        int maxInside = EnvReader.getInt("MAX_INSIDE");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");

        // nstancia o monitor 
        mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside);

        // Instancia a interface TCP 
        repoInterface = IRepo.getInstance(mrepo);

        // Cria o servidor TCP 
        server = new ServerCom(repoPort);
        server.start();
        System.out.println("Servidor Repository a escutar no porto " + repoPort + "...");

        // Aceitação de pedidos 
        while (true) {
            sconi = server.accept();
            proxy = new PRepo(sconi, repoInterface);
            new Thread(proxy).start();
        }
    }
    
}
