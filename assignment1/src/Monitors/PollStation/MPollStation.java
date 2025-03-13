package Monitors.PollStation;

import Monitors.Logs.ILogs;
import Monitors.Logs.MLogs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MPollStation implements IPollStation {
    private static MPollStation instance;
    private final Lock lock = new ReentrantLock();
    private final Condition canEnter = lock.newCondition();
    private final int maxCapacity;
    private PollStationState state;
    private final Queue<String> waitingFIFO = new LinkedList<>(); // FIFO para threads dentro da PollStation
    private final ILogs log = MLogs.getInstance(); // Instância do log

    private MPollStation(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.state = PollStationState.CLOSED_PS; // Começa fechada
    }

    public static IPollStation getInstance(int maxCapacity) {
        if (instance == null) {
            instance = new MPollStation(maxCapacity);
        }
        return instance;
    }
    
    @Override
    public String getPollState() {
        lock.lock();
        try {
            if (state == PollStationState.OPEN_PS)
                return "Open";
            else
                return "Closed";
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void enterPS(String voterID) throws InterruptedException {
        lock.lock();
        try {
            while (state != PollStationState.OPEN_PS || waitingFIFO.size() >= maxCapacity) {
                canEnter.await(); // Espera até a PollStation estar aberta e ter espaço
            }
            waitingFIFO.add(voterID); // Adiciona a thread ao FIFO
            log.log("[PollStation] - " + voterID + " entrou na PollStation.");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void exitPS(String voterID) {
        lock.lock();
        try {
            waitingFIFO.remove(voterID); // Remove o eleitor do FIFO interno
            canEnter.signal(); // Permite que outro Voter entre
            log.log("[PollStation] - " + voterID + " saiu da PollStation.");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void closeStation() {
        lock.lock();
        try {
            state = PollStationState.CLOSED_PS;
            canEnter.signalAll(); // Libera todas as Threads em espera
            log.log("[PollStation] - A votação foi encerrada!");
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void openStation() {
        lock.lock();
        try {
            state = PollStationState.OPEN_PS;
            canEnter.signalAll(); // Libera todas as Threads em espera
            log.log("[PollStation] - Aberta a PollStation!");
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean openFifo() {
        lock.lock();
        try {
            return waitingFIFO.size() < maxCapacity;
        } finally {
            lock.unlock();
        }
    }
    
}
