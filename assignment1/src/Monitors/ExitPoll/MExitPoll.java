package Monitors.ExitPoll;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MExitPoll implements IExitPoll {
    private static MExitPoll instance;
    private final Queue<String> exitPollQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private boolean electionOver = false; // 🔹 Estado da eleição

    private MExitPoll() {}

    public static IExitPoll getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }

    @Override
    public void addToExitPoll(String voterID) {
        lock.lock();
        try {
            if (!electionOver) { // 🔹 Só adicionamos enquanto a eleição estiver aberta
                exitPollQueue.add(voterID);
                System.out.println(voterID + " entrou na fila de exit poll.");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getNextVoter() {
        lock.lock();
        try {
            return exitPollQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean hasVoters() {
        lock.lock();
        try {
            return !exitPollQueue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void announceElectionEnd() {
        lock.lock();
        try {
            electionOver = true;
            System.out.println("Exit Poll: Eleições encerradas!");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isElectionOver() {
        lock.lock();
        try {
            return electionOver;
        } finally {
            lock.unlock();
        }
    }
}