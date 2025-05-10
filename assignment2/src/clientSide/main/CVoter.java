package clientSide.main;

import clientSide.entities.TVoter;
import clientSide.stubs.STEvotingBooth;
import clientSide.stubs.STExitPoll;
import clientSide.stubs.STIDCheck;
import clientSide.stubs.STPollStation;
import utils.EnvReader;

public class CVoter {

    public static void main(String[] args) {
        /* Lê parâmetros do ficheiro .env */
        String host = EnvReader.get("HOST");
        int psPort = EnvReader.getInt("POLLSTATION_PORT");
        int idCheckPort = EnvReader.getInt("IDCHECK_PORT");
        int boothPort = EnvReader.getInt("EVOTINGBOOTH_PORT");
        int exitPollPort = EnvReader.getInt("EXITPOLL_PORT");
        int numberOfVoters = EnvReader.getInt("NUM_VOTERS");

        var pollStation = STPollStation.getInstance(host, psPort);
        var idCheck = STIDCheck.getInstance(host, idCheckPort);
        var booth = STEvotingBooth.getInstance(host, boothPort);
        var exitPoll = STExitPoll.getInstance(host, exitPollPort);

        Thread[] voters = new Thread[numberOfVoters];
        for (int i = 0; i < numberOfVoters; i++) {
            voters[i] = new Thread(TVoter.getInstance(
                    "V" + (i + 1),
                    pollStation,
                    idCheck,
                    booth,
                    exitPoll));
            voters[i].start();
        }

        try {
            for (Thread v : voters) {
                v.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Voter thread interrupted: " + e.getMessage());
        }
    }
}
