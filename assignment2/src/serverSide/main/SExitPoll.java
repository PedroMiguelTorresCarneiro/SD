package serverSide.main;

import commInfra.ServerCom;
import serverSide.entities.PExitPoll;
import serverSide.sharedRegions.ExitPoll.IExitPoll;
import serverSide.sharedRegions.ExitPoll.MExitPoll;
import serverSide.stubs.SRepository;
import commInfra.interfaces.Repository.IRepo_ExitPoll;
import utils.EnvReader;
import java.net.SocketTimeoutException;

/**
 * Servidor da região partilhada ExitPoll com configuração via .env.
 * 
 * Autor: Pedro Carneiro
 */
public class SExitPoll {

    /**
     * Flag que sinaliza se o serviço está ativo.
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws SocketTimeoutException {
        ServerCom server = null, sconi;
        MExitPoll exitPoll;
        IExitPoll exitPollInterface;
        PExitPoll proxy;

        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");
        int exitPollPort = EnvReader.getInt("EXITPOLL_PORT");

        /* Instancia o stub remoto do repositório */
        IRepo_ExitPoll logs = SRepository.getInstance(host, repoPort);

        /* Instancia o monitor */
        exitPoll = MExitPoll.getInstance(logs);

        /* Instancia a interface TCP */
        exitPollInterface = IExitPoll.getInstance(exitPoll);

        /* Cria o servidor TCP */
        server = new ServerCom(exitPollPort);
        server.start();
        System.out.println("Servidor ExitPoll a escutar no porto " + exitPollPort + "...");

        /* Aceitação de pedidos */
        while (true) {
            sconi = server.accept();
            proxy = PExitPoll.getInstance(sconi, exitPollInterface);
            new Thread(proxy).start();
        }
    }
}
