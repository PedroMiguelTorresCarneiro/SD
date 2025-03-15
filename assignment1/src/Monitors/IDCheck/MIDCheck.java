package Monitors.IDCheck;

import Monitors.EvotingBooth.IEvotingBooth;
import Monitors.IAll;
import Threads.Voter.TVoter;
import java.util.concurrent.locks.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MIDCheck implements IIDCheck {
    private static MIDCheck instance;
    private final ReentrantLock lock;
    private Condition c1;
    private IEvotingBooth votingBooth;
    private List<String> listID = new ArrayList<String>();

    private MIDCheck() {
        lock = new ReentrantLock();
        // podemos criar as condições de paragem 
        c1 = lock.newCondition();
    }

    public IAll getInstance(){
        if (instance == null) {
            instance = new MIDCheck();
        }
        return instance;
    }
    
    @Override
    public void callAvoter(){
        // chamar um metodo da PS para acordar a thread
    }
    
    @Override
    public void RegiterID(TVoter t1){
        // regist ID
        listID.add(t1.getId());
    }
    
    @Override
    public boolean checkingID(TVoter t1){
        try {
            c1.wait(1325465); // simula um tempo de checkar o ID
        } catch (InterruptedException ex) {
            Logger.getLogger(MIDCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listID.contains(t1.getId());
    }
}
