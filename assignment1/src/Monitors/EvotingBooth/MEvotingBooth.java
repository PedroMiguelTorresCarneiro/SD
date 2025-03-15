package Monitors.EvotingBooth;

import Monitors.IAll;
import Threads.Voter.TVoter;

import java.util.HashMap;
import java.util.concurrent.locks.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MEvotingBooth implements IEvotingBooth {
    private static MEvotingBooth instance = null;
    private final HashMap<String, String> votes = new HashMap<>();
    private final ReentrantLock lock;
    private Condition c1;
    
    private MEvotingBooth() {
        lock = new ReentrantLock();
        // podemos criar as condições de paragem 
        c1 = lock.newCondition();
    }

    public IAll getInstance() {
        if (instance == null) {
            instance = new MEvotingBooth();
        }
        return instance;
    }

    @Override
    public void voting(TVoter t1) {
        try {
            // esperar um tempo
            c1.wait(452124);
        } catch (InterruptedException ex) {
            Logger.getLogger(MEvotingBooth.class.getName()).log(Level.SEVERE, null, ex);
        }
        // add vote to Votes
        votes.put(t1.getId(), t1.getVote());
    }
    
    @Override
    public String getVotesResult(){
        int countA = 0;
        int countB = 0;
        
        // Percorrer os valores do HashMap e contar os votos
        for (String vote : votes.values()) {
            if ("A".equals(vote)) {
                countA++;
            } else if ("B".equals(vote)) {
                countB++;
            }
        }
        
        // Retornar o resultado formatado
        return "A : " + countA + " - B : " + countB;
    }
}
