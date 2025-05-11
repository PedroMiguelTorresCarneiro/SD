package serverSide.main;

import commInfra.ServerCom;
import serverSide.entities.PIDCheck;
import serverSide.sharedRegions.IDCheck.IIDCheck;
import serverSide.sharedRegions.IDCheck.MIDCheck;
import serverSide.stubs.STRepository;
import commInfra.interfaces.Repository.IRepo_IDChek;
import utils.EnvReader;
import java.net.SocketTimeoutException;

/**
 * Servidor da região partilhada IDCheck com configuração via .env.
 * 
 * Autor: Pedro Carneiro
 */
public class SIDCheck {

    /**
     * Flag que sinaliza se o serviço está ativo.
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws SocketTimeoutException {
        ServerCom server = null, sconi;
        MIDCheck midCheck;
        IIDCheck iidCheck;
        PIDCheck proxy;

        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");
        int idCheckPort = EnvReader.getInt("IDCHECK_PORT");

        /* Instancia o stub remoto do repositório */
        IRepo_IDChek logs = STRepository.getInstance(host, repoPort);

        /* Instancia o monitor */
        midCheck = MIDCheck.getInstance(logs);

        /* Instancia a interface TCP */
        iidCheck = IIDCheck.getInstance(midCheck);

        /* Cria o servidor TCP */
        server = new ServerCom(idCheckPort);
        server.start();
        System.out.println("Servidor IDCheck a escutar no porto " + idCheckPort + "...");

        /* Aceitação de pedidos */
        while (true) {
            sconi = server.accept();
            proxy = new PIDCheck(sconi, iidCheck);
            new Thread(proxy).start();
        }
    }
}
