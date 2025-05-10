package clientSide.main;

import clientSide.entities.TPollClerk;
import clientSide.stubs.STEvotingBooth;
import clientSide.stubs.STExitPoll;
import clientSide.stubs.STPollStation;
import utils.EnvReader;

public class CPollClerk {

    public static void main(String[] args) {
        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int psPort = EnvReader.getInt("POLLSTATION_PORT");
        int boothPort = EnvReader.getInt("EVOTINGBOOTH_PORT");
        int exitPollPort = EnvReader.getInt("EXITPOLL_PORT");
        int maxVotes = EnvReader.getInt("VOTES_TO_END");

        /* Instancia os stubs */
        var pollStation = STPollStation.getInstance(host, psPort);
        var booth = STEvotingBooth.getInstance(host, boothPort);
        var exitPoll = STExitPoll.getInstance(host, exitPollPort);

        /* Inicia a thread do funcionário */
        Thread clerk = new Thread(TPollClerk.getInstance(pollStation, booth, exitPoll, maxVotes));
        clerk.start();

        try {
            clerk.join();
        } catch (InterruptedException e) {
            System.out.println("Poll Clerk thread interrupted: " + e.getMessage());
        }
    }
}
