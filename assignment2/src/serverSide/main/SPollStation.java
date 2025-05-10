package serverSide.main;

import commInfra.ServerCom;
import serverSide.entities.PPollStation;
import serverSide.sharedRegions.PollStation.IPollStation;
import serverSide.sharedRegions.PollStation.MPollStation;
import serverSide.stubs.SRepository;
import commInfra.interfaces.Repository.IRepo_PollStation;
import utils.EnvReader;
import java.net.SocketTimeoutException;

/**
 * Servidor da região partilhada PollStation com configuração via .env.
 * 
 * Autor: Pedro Carneiro
 */
public class SPollStation {

    /**
     * Flag que sinaliza se o serviço está ativo.
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws SocketTimeoutException {
        ServerCom server = null, sconi;
        MPollStation pollStation;
        IPollStation pollStationInterface;
        PPollStation proxy;

        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");
        int maxInside = EnvReader.getInt("MAX_INSIDE");
        int pollStationPort = EnvReader.getInt("POLLSTATION_PORT");

        /* Instancia o stub remoto do repositório */
        IRepo_PollStation logs = SRepository.getInstance(host, repoPort);

        /* Instancia o monitor com capacidade máxima */
        pollStation = MPollStation.getInstance(maxInside, logs);

        /* Instancia a interface TCP */
        pollStationInterface = IPollStation.getInstance(pollStation);

        /* Cria o servidor TCP */
        server = new ServerCom(pollStationPort);
        server.start();
        System.out.println("Servidor PollStation a escutar no porto " + pollStationPort + "...");

        /* Aceitação de pedidos */
        while (true) {
            sconi = server.accept();
            proxy = PPollStation.getInstance(sconi, pollStationInterface);
            new Thread(proxy).start();
        }
    }
}
