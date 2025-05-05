package clientSide.main;

import clientSide.entities.TPollster;
import clientSide.stubs.ExitPoll.STExitPoll_TPollster;

public class CPollster {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java CPollster <host> <exitPollPort>");
            System.exit(1);
        }

        String host = args[0];
        int exitPollPort = Integer.parseInt(args[1]);

        var exitPoll = STExitPoll_TPollster.getInstance(host, exitPollPort);
        Thread pollster = new Thread(TPollster.getInstance(exitPoll));
        pollster.start();
        
        try {
            pollster.join();
        } catch (InterruptedException e) {
            System.out.println("Pollster thread interrupted: " + e.getMessage());
        }
    }
}
