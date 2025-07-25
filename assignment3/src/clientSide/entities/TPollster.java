package clientSide.entities;

import commInfra.interfaces.ExitPoll.IExitPoll_TPollster;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The TPollster class implements the Runnable interface and represents the life cycle of a pollster in the election simulation.
 * The pollster's life cycle is modeled as a finite state machine, where the pollster can be in one of the following states:
 * WAITING_VOTERS, SELECT_VOTER, or PUBLISHING_RESULTS.
 * The pollster interacts with the exit poll shared region to conduct surveys and publish results.
 * This class also provides a method to change the pollster's state.
 *
 * The TPollster class follows the Singleton design pattern, ensuring that only one instance of the pollster exists in the simulation.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class TPollster implements Runnable {
    /**
     * The exitPoll attribute stores a reference to the exit poll shared region.
     * This is the interface that the pollster uses to interact with the exit poll.
     */
    private final IExitPoll_TPollster exitPoll;

    /**
     * The instance attribute stores the unique instance of the TPollster class.
     * This attribute is used to implement the Singleton design pattern, ensuring that only one pollster exists in the simulation.
     */
    private static TPollster instance = null;

    /**
     * The state attribute stores the current state of the pollster.
     * The initial state is WAITING_VOTERS, where the pollster waits for voters to conduct the survey.
     */
    private PollsterState state = PollsterState.WAITING_VOTERS;

    /**
     * The PollsterState enum defines the possible states of the pollster during the simulation.
     */
    public static enum PollsterState {
        /**
         * The WAITING_VOTERS state represents the state where the pollster waits for voters to conduct the survey.
         */
        WAITING_VOTERS,
        /**
         * The PUBLISHING_RESULTS state represents the state where the pollster publishes the results of the survey and ends its life cycle.
         */
        PUBLISHING_RESULTS
    }

    /**
     * The TPollster constructor initializes the pollster object with the specified attribute.
     *
     * @param exitPoll The exit poll shared region.
     */
    private TPollster(IExitPoll_TPollster exitPoll) {
        this.exitPoll = exitPoll;
    }

    /**
     * The getInstance method returns the unique instance of the TPollster class.
     * If the instance does not exist, it creates a new one.
     *
     * @param exitPoll The exit poll shared region.
     * @return The unique instance of the TPollster class.
     */
    public static Runnable getInstance(IExitPoll_TPollster exitPoll) {
        if (instance == null) {
            instance = new TPollster(exitPoll);
        }
        return instance;
    }

    /**
     * The run method implements the life cycle of the pollster.
     * The pollster transitions between states based on interactions with the exit poll shared region.
     * This cycle ends when the pollster publishes the results of the survey (PUBLISHING_RESULTS state).
     *
     * The pollster can be in one of the following states:
     * WAITING_VOTERS: 
     *      If the exit poll is open The pollster waits for voters and when a voter arrives, it conducts the survey, then waits for new one
     *      Otherwise , the pollster state is set to the PUBLISHNG_RESULTS and ends its life cycle.
     */
    @Override
    public void run() {
        try {
            while (state != PollsterState.PUBLISHING_RESULTS) {
                switch (state) {
                    case WAITING_VOTERS -> {
                        System.out.println("[TPOLLSTER] - CASE WAITING_VOTERS --->");
                        if (!exitPoll.isOpen()) {
                            System.out.println("[TPOLLSTER] - Exit poll is closed, publishing results...");
                            setState(PollsterState.PUBLISHING_RESULTS);
                            break;
                        }


                        exitPoll.conductSurvey();
                        System.out.println("[TPOLLSTER] - TPollster called a voter to conduct the survey...");
                    }
                    default -> {
                    }
                }
            }
            

            System.out.println("[TPOLLSTER] - TPollster is publishing results...");
            exitPoll.publishResults();
            setState(PollsterState.PUBLISHING_RESULTS);
            resetInstance();
        } catch (RemoteException ex) {
            Logger.getLogger(TPollster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The setState method updates the pollster's current state.
     *
     * @param state The new state of the pollster.
     */
    private void setState(PollsterState state) {
        this.state = state;
    }
    
    /**
     * The resetInstance method resets the instance of the TPollSter class to null.
     * So this thread can be reused in the next election.
     */
    private static void resetInstance(){
        instance = null;
    }
}