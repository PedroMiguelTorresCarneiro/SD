package Monitors.IDCheck;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MIDCheck implements IIDCheck {
    private static MIDCheck instance;
    private final Set<String> idsRegistrados = new HashSet<>();
    private final Lock lock ;

    private MIDCheck() {
        lock = new ReentrantLock();
    }

    public static IIDCheck getInstance(){
        if (instance == null) {
            instance = new MIDCheck();
        }
        return instance;
    }
    
}
