package Monitors.ExitPoll;
// packages
import Monitors.IAll;
import Threads.Voter.TVoter;
// libs
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MExitPoll implements IExitPoll {
    private static MExitPoll instance = null;
    private final Queue<String> exitPollQueue = new LinkedList<>();
    private final ReentrantLock lock;
    private Condition c1;
    private final Random random = new Random();

    private MExitPoll() {
        lock = new ReentrantLock();
        // podemos criar as condições de paragem 
        c1 = lock.newCondition();
    }
    
    public IAll getInstance() {
        if (instance == null) {
            instance = new MExitPoll();
        }
        return instance;
    }
    
    public void callVoterExitPoll(){
        // chama uma thread na exitPollQueue
        c1.notify();
    }
    
    public void receiveVoterAnswer(TVoter t1){
        // guarda o voto algures
        exitPollQueue.add(t1.getVote());
    }
    
    public void questionsForVotes(){
        try {
            // esperar um tempo para simular a resposta
            c1.wait(12453);
        } catch (InterruptedException ex) {
            Logger.getLogger(MExitPoll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reborn(TVoter t1){
        // calcular uma percentagem de reborn com o mesmo ID
        boolean sameID = random.nextDouble() > 0.5;
        if (sameID) {
            // update do Voto
            t1.setVote();
        } else {
            // novo ID
            t1.setId();
            // update voto
            t1.setVote();
        }
    }
}