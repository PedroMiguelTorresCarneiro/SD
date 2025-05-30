package clientSide.main;

import clientSide.entities.TPollster;
import clientSide.stubs.STExitPoll;
import utils.EnvReader;

public class CPollster {

    public static void main(String[] args) {
        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int exitPollPort = EnvReader.getInt("EXITPOLL_PORT");

        /* Instancia stub remoto */
        var exitPoll = STExitPoll.getInstance(host, exitPollPort);

        /* Inicia a thread TPollster */
        Thread pollster = new Thread(TPollster.getInstance(exitPoll));
        pollster.start();

        try {
            pollster.join();
        } catch (InterruptedException e) {
            System.out.println("Pollster thread interrupted: " + e.getMessage());
        }
    }
}
