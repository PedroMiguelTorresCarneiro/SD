package Monitors.IDCheck;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.Logs.ILogs;
import Monitors.Logs.MLogs;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MIDCheck implements IIDCheck {
    private static MIDCheck instance;
    private final Lock lock = new ReentrantLock(); // Garante exclusividade no check ID
    private final ILogs log = MLogs.getInstance();
    private final IEvotingBooth votingBooth;

    private MIDCheck(IEvotingBooth votingBooth) {
        this.votingBooth = votingBooth;
    }

    public static IIDCheck getInstance(IEvotingBooth votingBooth) {
        if (instance == null) {
            instance = new MIDCheck(votingBooth);
        }
        return instance;
    }

    @Override
    public boolean checkingID(String voterID) {
        lock.lock(); // 🔒 Garante que só um `Voter` faz check de ID ao mesmo tempo
        try {
            log.log("[IDCheck] - Verificando ID de " + voterID);
            Thread.sleep(500); // Simula tempo de verificação

            if (votingBooth.isRegistered(voterID)) {
                log.log("[IDCheck] - " + voterID + " já está registado! ID inválido.");
                return false; // ID já existe
            }

            log.log("[IDCheck] - " + voterID + " pode votar.");
            return true; // ID ainda não existe
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock(); // 🔓 Libera para outro `Voter`
        }
    }
}
