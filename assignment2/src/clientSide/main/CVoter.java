package clientSide.main;

import clientSide.entities.TVoter;
import clientSide.stubs.EvotingBooth.STEVotingBooth_TVoter;
import clientSide.stubs.ExitPoll.STExitPoll_TVoter;
import clientSide.stubs.IDCheck.STIDCheck_TVoter;
import clientSide.stubs.PollStation.STPollStation_TVoter;

public class CVoter {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Usage: java CVoter <host> <psPort> <idCheckPort> <boothPort> <exitPollPort> <numberOfVoters>");
            System.exit(1);
        }

        String host = args[0];
        int psPort = Integer.parseInt(args[1]);
        int idCheckPort = Integer.parseInt(args[2]);
        int boothPort = Integer.parseInt(args[3]);
        int exitPollPort = Integer.parseInt(args[4]);
        int numberOfVoters = Integer.parseInt(args[5]);

        var pollStation = STPollStation_TVoter.getInstance(host, psPort);
        var idCheck = STIDCheck_TVoter.getInstance(host, idCheckPort);
        var booth = STEVotingBooth_TVoter.getInstance(host, boothPort);
        var exitPoll = STExitPoll_TVoter.getInstance(host, exitPollPort);

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

        try{
            for (Thread v : voters) {
                v.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Voter thread interrupted: " + e.getMessage());
        }

    }
}
