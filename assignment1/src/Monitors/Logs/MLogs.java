package Monitors.Logs;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MLogs implements ILogs {
    private static MLogs instance;
    private final Lock lock = new ReentrantLock();

    private MLogs() {}

    public static ILogs getInstance() {
        if (instance == null) {
            instance = new MLogs();
        }
        return instance;
    }

    @Override
    public void log(String message) {
        lock.lock();
        try {
            System.out.println("-> " + message);
        } finally {
            lock.unlock();
        }
    }
}