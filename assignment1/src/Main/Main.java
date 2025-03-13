package Main;

import Monitors.PollStation.*;
import Monitors.IDCheck.*;
import Monitors.EvotingBooth.*;
import Monitors.Logs.*;
import Monitors.ExitPoll.*;
import Threads.Voter.*;
import Threads.PollClerk.*;
import Threads.Pollster.*;

public class Main {
    public static void main(String[] args) {
        int numVoters = 3;   // Número de eleitores a serem criados
        int maxCapacity = 2;  // Capacidade máxima dentro da PollStation
        int maxVotes = 4;    // Número total de votos antes de fechar a votação

        // 🔹 Criar os monitores (Shared Regions)
        ILogs log = MLogs.getInstance();
        IEvotingBooth votingBooth = MEvotingBooth.getInstance();
        IIDCheck idCheck = MIDCheck.getInstance(votingBooth);
        IExitPoll exitPoll = MExitPoll.getInstance();
        IPollStation pollStation = MPollStation.getInstance(maxCapacity);

        // 🔹 Criar e iniciar os eleitores (TVoter)
        Thread[] voters = new Thread[numVoters];
        for (int i = 0; i < numVoters; i++) {
            String voterID = "V" + i;
            String vote = Math.random() > 0.5 ? "A" : "B"; // Escolhe aleatoriamente Party A ou Party B
            voters[i] = new Thread(new TVoter(voterID, vote, pollStation, idCheck, votingBooth, exitPoll, log));
            voters[i].start();
        }

        // 🔹 Criar e iniciar o PollClerk
        Thread pollClerk = new Thread(new TPollClerk(pollStation, exitPoll, log, maxVotes));
        pollClerk.start();

        // 🔹 Criar e iniciar o Pollster
        Thread pollster = new Thread(new TPollster(exitPoll, log));
        pollster.start();

        // 🔹 Esperar que todas as Threads terminem
        try {
            for (Thread voter : voters) {
                voter.join();
            }
            pollClerk.join();
            pollster.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.log("[Main] - Simulação encerrada.");
    }
}
