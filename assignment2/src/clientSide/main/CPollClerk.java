package clientSide.main;

import clientSide.entities.TPollClerk;
import clientSide.stubs.EvotingBooth.STEVotingBooth_TPollClerk;
import clientSide.stubs.ExitPoll.STExitPoll_TPollClerk;
import clientSide.stubs.PollStation.STPollStation_TPollClerk;

public class CPollClerk {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Usage: java CPollClerk <host> <psPort> <boothPort> <exitPollPort> <maxVotes>");
            System.exit(1);
        }

        String host = args[0];
        int psPort = Integer.parseInt(args[1]);
        int boothPort = Integer.parseInt(args[2]);
        int exitPollPort = Integer.parseInt(args[3]);
        int maxVotes = Integer.parseInt(args[4]);

        var pollStation = STPollStation_TPollClerk.getInstance(host, psPort);
        var booth = STEVotingBooth_TPollClerk.getInstance(host, boothPort);
        var exitPoll = STExitPoll_TPollClerk.getInstance(host, exitPollPort);

        Thread clerk = new Thread(TPollClerk.getInstance(pollStation, booth, exitPoll, maxVotes));
        clerk.start();
        
        try {
            clerk.join();
        } catch (InterruptedException e) {
            System.out.println("Poll Clerk thread interrupted: " + e.getMessage());
        }
    }
}
