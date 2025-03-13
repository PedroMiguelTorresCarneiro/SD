package Threads.Pollster;

import Monitors.ExitPoll.IExitPoll;
import Monitors.Logs.ILogs;
import java.util.Random;

public class TPollster implements Runnable {
    private final IExitPoll exitPoll;
    private final ILogs log;
    private final Random random = new Random();

    public TPollster(IExitPoll exitPoll, ILogs log) {
        this.exitPoll = exitPoll;
        this.log = log;
    }

    @Override
    public void run() {
        log.log("[Pollster] - Aguardando eleitores na saída...");

        while (!exitPoll.isElectionOver()) {
            try {
                Thread.sleep(1000);
                String voterID = exitPoll.getNextVoter();
                if (voterID != null) {
                    boolean truthful = random.nextBoolean();
                    log.log("[Pollster] - Entrevistou " + voterID + " que disse " + (truthful ? "a verdade" : "uma mentira"));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.log("[Pollster] - Pesquisa encerrada.");
    }
}
