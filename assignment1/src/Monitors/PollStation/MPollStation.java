package Monitors.PollStation;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MPollStation implements IPollStation {
    private boolean fechada = false;
    private final Lock lock;
    private String votanteAtual = null;
    private static MPollStation instance = null;
    private final int capacidadeMax;

    private MPollStation(int capacidadeMax) {
        lock = new ReentrantLock();
        // Condições

        this.capacidadeMax = capacidadeMax;
        
    }
    
    public static IPollStation getInstance(int capacidadeMax) {
        if (instance == null) {
            instance =  new MPollStation(capacidadeMax);
        }
        return instance;
    }

}
