package Monitors.EvotingBooth;

import Monitors.Logs.ILogs;
import Monitors.Logs.MLogs;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MEvotingBooth implements IEvotingBooth {
    private static MEvotingBooth instance;
    private final Lock lock = new ReentrantLock(); // 🔒 Garante que só um `Voter` está a votar por vez
    private final HashMap<String, String> votes = new HashMap<>();
    private final ILogs log = MLogs.getInstance();

    private MEvotingBooth() {}

    public static IEvotingBooth getInstance() {
        if (instance == null) {
            instance = new MEvotingBooth();
        }
        return instance;
    }

    @Override
    public void voting(String voterID, String vote) {
        lock.lock(); // 🔒 Garante que só um `Voter` está a votar ao mesmo tempo
        try {
            log.log("[EvotingBooth] - " + voterID + " está a votar...");
            Thread.sleep(1000); // Simula tempo de votação
            votes.put(voterID, vote);
            log.log("[EvotingBooth] - " + voterID + " votou em " + vote);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock(); // 🔓 Libera para outro `Voter`
        }
    }

    @Override
    public boolean isRegistered(String voterID) {
        return votes.containsKey(voterID); // Apenas leitura, sem necessidade de lock
    }
}
