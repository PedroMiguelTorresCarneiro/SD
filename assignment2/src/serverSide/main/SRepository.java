package serverSide.main;

import commInfra.ServerCom;
import serverSide.entities.PRepo;
import serverSide.sharedRegions.Repository.IRepo;
import serverSide.sharedRegions.Repository.MRepo;
import utils.EnvReader;
import java.net.SocketTimeoutException;

/**
 * Servidor da região partilhada Repository com GUI e configuração via .env.
 * 
 * Autor: Pedro Carneiro
 */
public class SRepository {

    /**
     * Flag que sinaliza se o serviço está ativo.
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws SocketTimeoutException {
        ServerCom server = null, sconi;
        MRepo mrepo;
        IRepo repoInterface;
        PRepo proxy;
        mainGUI gui;

        /* Lê parâmetros do ficheiro .env */
        int votesToEnd = EnvReader.getInt("VOTES_TO_END");
        int numVoters = EnvReader.getInt("NUM_VOTERS");
        int maxInside = EnvReader.getInt("MAX_INSIDE");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");

        /* Instancia a GUI */
        gui = new mainGUI();

        /* Instancia o monitor */
        mrepo = MRepo.getInstance(votesToEnd, numVoters, maxInside, gui);

        /* Instancia a interface TCP */
        repoInterface = IRepo.getInstance(mrepo);

        /* Cria o servidor TCP */
        server = new ServerCom(repoPort);
        server.start();
        System.out.println("Servidor Repository a escutar no porto " + repoPort + "...");

        /* Aceitação de pedidos */
        while (true) {
            sconi = server.accept();
            proxy = new PRepo(sconi, repoInterface);
            new Thread(proxy).start();
        }
    }
}
