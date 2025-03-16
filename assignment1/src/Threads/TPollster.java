package Threads;

import Monitors.ExitPoll.IExitPoll;
import java.util.Random;

public class TPollster extends Thread {
    private final IExitPoll exitPoll;
    private final Random random = new Random();

    public TPollster(IExitPoll exitPoll) {
        this.exitPoll = exitPoll;
    }

    @Override
    public void run() {
        try {
            while (!exitPoll.isEncerrado()) {
                String voterId = exitPoll.entrevistarProximo();
                if (voterId == null) break; // Se a eleição acabou, termina a thread

            }
            System.out.println("⏹ TPollster terminou o seu trabalho!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
