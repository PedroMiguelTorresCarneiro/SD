package Monitors.ExitPoll;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MExitPoll implements IExitPoll {
    private static MExitPoll instance = null;
    private final ReentrantLock lock;
    private final Queue<String> filaSaida = new LinkedList<>(); // Votantes que saíram da estação
    private final Map<String, String> votosReais = new HashMap<>(); // Armazena o voto real do votante
    private int votosA = 0;
    private int votosB = 0;
    private boolean eleicaoEncerrada = false;
    private final Condition pollsterPodeEntrevistar; // Condition para entrevistas
    private final Random random = new Random();

    private MExitPoll() {
        lock = new ReentrantLock();
        // Condições
        pollsterPodeEntrevistar = lock.newCondition();
    }
    
    public static IExitPoll getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }

    @Override
    public boolean open() {
        return true;
    }

    @Override
    public void announceResults() {
    }

    @Override
    public void conductSurvey() {
    }

    @Override
    public void waitForVoters() {
    }

    @Override
    public void publishResults() {
    }
    
    
}