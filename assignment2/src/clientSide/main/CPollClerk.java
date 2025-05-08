package clientSide.main;

import clientSide.entities.TPollClerk;
import clientSide.stubs.STEvotingBooth;
import clientSide.stubs.STExitPoll;
import clientSide.stubs.STPollStation;


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

        var pollStation = STPollStation.getInstance(host, psPort);
        var booth = STEvotingBooth.getInstance(host, boothPort);
        var exitPoll = STExitPoll.getInstance(host, exitPollPort);

        Thread clerk = new Thread(TPollClerk.getInstance(pollStation, booth, exitPoll, maxVotes));
        clerk.start();
        
        try {
            clerk.join();
        } catch (InterruptedException e) {
            System.out.println("Poll Clerk thread interrupted: " + e.getMessage());
        }
    }
}
