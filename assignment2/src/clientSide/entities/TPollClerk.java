package clientSide.entities;

import Monitors.EvotingBooth.IEVotingBooth_TPollClerk;
import Monitors.ExitPoll.IExitPoll_TPollClerk;
import Monitors.PollStation.IPollStation_TPollClerk;

/**
 * The TPollClerk class implements the Runnable interface and represents the life cycle of a poll clerk in the election simulation.
 * The poll clerk's life cycle is modeled as a finite state machine, where the poll clerk can be in one of the following states:
 * ID_CHECK_WAIT, CLOSING_PS, OPEN_PS, ID_CHECK, INFORMING_EP, GATHERING_VOTES, or PUBLISHING_WINNER.
 * The poll clerk interacts with shared regions such as the polling station, ID check, voting booth, and exit poll.
 * This class also provides methods to change the poll clerk's state and retrieve the current state.
 *
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class TPollClerk implements Runnable, TPollClerkCloning {

    /**
     * The pollStation attribute stores a reference to the polling station shared region.
     * This is the interface that the poll clerk uses to interact with the polling station.
     */
    private final IPollStation_TPollClerk pollStation;

    /**
     * The booth attribute stores a reference to the voting booth shared region.
     * This is the interface that the poll clerk uses to interact with the voting booth.
     */
    private final IEVotingBooth_TPollClerk booth;

    /**
     * The exitPoll attribute stores a reference to the exit poll shared region.
     * This is the interface that the poll clerk uses to interact with the exit poll.
     */
    private final IExitPoll_TPollClerk exitPoll;

    /**
     * The instance attribute stores the unique instance of the TPollClerk class.
     * This attribute is used to implement the Singleton design pattern, ensuring that only one poll clerk exists in the simulation.
     */
    private static TPollClerk instance = null;

    /**
     * The state attribute stores the current state of the poll clerk.
     * The initial state is OPEN_PS, where the poll clerk opens the polling station.
     */
    private PollClerkState state = PollClerkState.OPEN_PS;

    /**
     * The maxVotes attribute stores the maximum number of votes required to trigger the end of the elections.
     */
    private final int maxVotes;

    /**
     * The PollClerkState enum defines the possible states of the poll clerk during the simulation.
     */
    public static enum PollClerkState {
        /**
         * The ID_CHECK_WAIT state represents the state where the poll clerk waits for the next voter to check their ID.
         */
        ID_CHECK_WAIT, 
        /**
         * The CLOSING_PS state represents the state where the poll clerk closes the polling station.
         */
        CLOSING_PS,
        /**
         * The OPEN_PS state represents the state where the poll clerk opens the polling station.
         */
        OPEN_PS,
        /**
         * The ID_CHECK state represents the state where the poll clerk checks the voter's ID.
         */
        ID_CHECK,
        /**
         * The INFORMING_EP state represents the state where the poll clerk informs the exit poll that the election is over.
         */
        INFORMING_EP,
        /**
         * The GATHERING_VOTES state represents the state where the poll clerk gathers the votes from the voting booth.
         */
        GATHERING_VOTES,
        /**
         * The PUBLISHING_WINNER state represents the state where the poll clerk publishes the winner of the elections.
         */
        PUBLISHING_WINNER
    }

    /**
     * The TPollClerk constructor initializes the poll clerk object with the specified attributes.
     *
     * @param pollStation The polling station shared region.
     * @param booth The voting booth shared region.
     * @param exitPoll The exit poll shared region.
     * @param maxVotes The maximum number of votes required to trigger the end of the elections.
     */
    private TPollClerk(IPollStation_TPollClerk pollStation, IEVotingBooth_TPollClerk booth, IExitPoll_TPollClerk exitPoll, int maxVotes) {
        this.pollStation = pollStation;
        this.booth = booth;
        this.exitPoll = exitPoll;
        this.maxVotes = maxVotes;
    }

    /**
     * The getInstance method returns the unique instance of the TPollClerk class.If the instance does not exist, it creates a new one.
     *
     * @param pollStation The polling station shared region.
     * @param booth The voting booth shared region.
     * @param exitPoll The exit poll shared region.
     * @param maxVotes The maximum number of votes required to trigger the end of the elections.
     * @return The unique instance of the TPollClerk class.
     */
    public static Runnable getInstance(IPollStation_TPollClerk pollStation, IEVotingBooth_TPollClerk booth, IExitPoll_TPollClerk exitPoll, int maxVotes) {
        if (instance == null) {
            instance = new TPollClerk(pollStation, booth, exitPoll, maxVotes);
        }

        return instance;
    }

    /**
     * The run method implements the life cycle of the poll clerk.
     * The poll clerk transitions between states based on interactions with the shared regions.
     * This cycle ends when the poll clerk publishes the winner of the elections (PUBLISHING_WINNER state).
     *
     * The poll clerk can be in one of the following states:
     * - OPEN_PS: The poll clerk opens the polling station and its state is set to ID_CHECK_WAIT.
     * 
     * - ID_CHECK_WAIT: The poll clerk calls the next voter to the ID check and its state is set to ID_CHECK.
     *                  But before that it checks if the polling station is closed, if it is, its state is set to INFORMING_EP.
     * 
     * - ID_CHECK: The poll clerk checks the voter's ID and waits for a new one, its state is set to ID_CHECK_WAIT.
     *             If the voting booth is full, the poll clerk closes the polling station.
     *             If the polling station is empty, the poll clerk informs the exit poll that the election is over and its state is changed to INFORMING_EP.
     *
     * - INFORMING_EP: The poll clerk closes the exit poll and gathers the votes from the voting booth, its state is set to GATHERING_VOTES.
     * - GATHERING_VOTES: The poll clerk publishes the election results and its state is set to PUBLISHING_WINNER.
     */
    @Override
    public void run() {
        try {
            while (state != PollClerkState.PUBLISHING_WINNER) {
                switch (state) {   
                    case OPEN_PS ->{
                        pollStation.openPS();
                        setState(PollClerkState.ID_CHECK_WAIT);
                    } 

                    case ID_CHECK_WAIT -> {           
                        if(pollStation.isPSclosedAfter()){
                            setState(PollClerkState.INFORMING_EP);
                            break;
                        } 
                        pollStation.callNextVoter();
                        setState(PollClerkState.ID_CHECK);
                    }

                    case ID_CHECK -> {
                        if (booth.getVotesCount() >= maxVotes) {
                            pollStation.closePS();
                        }

                        if (pollStation.isEmpty()) {
                            setState(PollClerkState.INFORMING_EP);
                            break;
                        }

                        setState(PollClerkState.ID_CHECK_WAIT);
                    }

                    case INFORMING_EP -> {
                        exitPoll.close();
                        booth.gathering();
                        setState(PollClerkState.GATHERING_VOTES);
                    }

                    case GATHERING_VOTES ->{
                        booth.publishElectionResults();
                        setState(PollClerkState.PUBLISHING_WINNER);
                    }

                    default -> {
                    }
                }
            }

            resetInstance();     
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The setState method updates the poll clerk's current state.
     *
     * @param state The new state of the poll clerk.
     */
    private void setState(PollClerkState state) {
        this.state = state;
    }

    /**
     * The getState method returns the current state of the poll clerk.
     *
     * @return The current state of the poll clerk.
     */
    private static void resetInstance(){
        instance = null;
    }
}