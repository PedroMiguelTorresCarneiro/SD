package Threads;

import Monitors.ExitPoll.IExitPoll_TPollster;

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
        WAITING_VOTERS,
        SELECT_VOTER,
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
     *      The pollster waits for voters to conduct the survey if the exit poll is open.
     *      If the exit poll is closed, the pollster publishes the results and ends its life cycle.

     * SELECT_VOTER: 
     *      The pollster waits for the exit poll to select a voter to answer the survey.
     *
     * @throws InterruptedException If any thread is interrupted during the simulation.
     */
    @Override
    public void run() {
        try {
            while (state != PollsterState.PUBLISHING_RESULTS) {
                switch (state) {
                    case WAITING_VOTERS -> {
                        if (!exitPoll.isOpen()) {
                            setState(PollsterState.PUBLISHING_RESULTS);
                            break;
                        }

                        exitPoll.conductSurvey(this);
                    }

                    case SELECT_VOTER -> exitPoll.waitForVoters(this);

                    default -> {
                    }
                }
            }
            
            exitPoll.publishResults(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The setState method updates the pollster's current state.
     *
     * @param state The new state of the pollster.
     */
    public void setState(PollsterState state) {
        this.state = state;
    }
}