package serverSide.main;

import commInfra.ServerCom;
import serverSide.entities.PEvotingBooth;
import serverSide.stubs.STRepository;
import commInfra.interfaces.Repository.IRepo_VotingBooth;
import java.net.SocketTimeoutException;
import serverSide.sharedRegions.EVotingBooth.MEvotingBooth;
import serverSide.sharedRegions.EVotingBooth.IEVotingBooth;
import utils.EnvReader;

/**
 * Servidor da região partilhada EvotingBooth com configuração via .env.
 * 
 */
public class SEvotingBooth {

    /**
     * Flag que sinaliza se o serviço está ativo.
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws SocketTimeoutException {
        ServerCom server = null, sconi;
        MEvotingBooth booth;
        IEVotingBooth boothInterface;
        PEvotingBooth proxy;

        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int repoPort = EnvReader.getInt("REPOSITORY_PORT");
        int boothPort = EnvReader.getInt("EVOTINGBOOTH_PORT");

        /* Instancia o stub remoto do repositório */
        IRepo_VotingBooth logs = STRepository.getInstance(host, repoPort);

        /* Instancia o monitor */
        booth = MEvotingBooth.getInstance(logs);

        /* Instancia a interface TCP */
        boothInterface = IEVotingBooth.getInstance(booth);

        /* Cria o servidor TCP */
        server = new ServerCom(boothPort);
        server.start();
        System.out.println("Servidor EvotingBooth a escutar no porto " + boothPort + "...");

        /* Aceitação de pedidos */
        while (true) {
            sconi = server.accept();
            proxy = new PEvotingBooth(sconi, boothInterface);
            new Thread(proxy).start();
        }
    }
}
