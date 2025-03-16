package Monitors.EvotingBooth;

import Monitors.IAll;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MEvotingBooth implements IEvotingBooth {
    private static MEvotingBooth instance = null;
    private final Map<String, String> votos = new HashMap<>();
    private final ReentrantLock lock;
    
    private MEvotingBooth() {
        lock = new ReentrantLock();
    }

    public static MEvotingBooth getInstance() {
        if (instance == null) {
            instance = new MEvotingBooth();
        }
        return instance;
    }

    public String votar(String voterId) throws InterruptedException {
        lock.lock();
        try {
            //System.out.println("🗳 Votante " + voterId + " está votando...");
            String voto = (Math.random() > 0.4) ? "A" : "B";
            votos.put(voterId, voto);
            
            //logs.atualizarEstado(voterId, "IDCheck", "VotingBooth"); // Registra a mudança de fila
            //logs.atualizarEstado(voterId, "VotingBooth", "");    // Registra a mudança de fila

            Thread.sleep((long) (Math.random() * 16)); 
            
            //logs.atualizarEstado(voterId, "VotingBooth", "Vote", voto); // Registra o voto do votante

            System.out.println("✅ Votante " + voterId + " terminou de votar. Voto: " + voto);
            return voto;
        } finally {
            lock.unlock();
        }
    }

    public int getTotalVotos() {
        lock.lock();
        try {
            return votos.size(); // Retorna a quantidade total de votos
        } finally {
            lock.unlock();
        }
    }

    public Map<String, String> gatherVotes() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("📊 Contando votos...");
            Thread.sleep(2000);
            return new HashMap<>(votos);
        } finally {
            lock.unlock();
        }
    }
}
