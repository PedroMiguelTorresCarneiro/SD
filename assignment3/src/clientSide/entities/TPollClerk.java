package clientSide.entities;


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
import clientSide.stubs.STEvotingBooth;
import clientSide.stubs.STExitPoll;
import clientSide.stubs.STIDCheck;
import clientSide.stubs.STPollStation;

public class TPollClerk implements Runnable {

    /**
     * The pollStation attribute stores a reference to the polling station shared region.
     * This is the interface that the poll clerk uses to interact with the polling station.
     */
    private final STPollStation pollStation;

    /**
     * The booth attribute stores a reference to the voting booth shared region.
     * This is the interface that the poll clerk uses to interact with the voting booth.
     */
    private final STEvotingBooth booth;

    /**
     * The exitPoll attribute stores a reference to the exit poll shared region.
     * This is the interface that the poll clerk uses to interact with the exit poll.
     */
    private final STExitPoll exitPoll;

    
    private final STIDCheck idCheck;
    
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
    private TPollClerk(STPollStation pollStation, STEvotingBooth booth, STExitPoll exitPoll, int maxVotes, STIDCheck idCheck) {
        this.pollStation = pollStation;
        this.booth = booth;
        this.exitPoll = exitPoll;
        this.maxVotes = maxVotes;
        this.idCheck = idCheck;
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
    public static Runnable getInstance(STPollStation pollStation, STEvotingBooth booth, STExitPoll exitPoll, int maxVotes, STIDCheck idCheck) {
        if (instance == null) {
            instance = new TPollClerk(pollStation, booth, exitPoll, maxVotes, idCheck);
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
                        System.out.println("\n[TPOLLCLERK] - CASE OPEN_PS --->\n");
                        pollStation.openPS();
                        setState(PollClerkState.ID_CHECK_WAIT);
                    } 

                    case ID_CHECK_WAIT -> { 
                        System.out.println("\n[TPOLLCLERK] - CASE ID_CHECK_WAIT --->\n");          
                        if(pollStation.isPSclosedAfter()){
                            System.out.println("\n[TPOLLCLERK] - Polling station is closed --->\n");
                            setState(PollClerkState.INFORMING_EP);
                            break;
                        } 

                        System.out.println("\n[TPOLLCLERK] - calling next voter --->\n");
                        pollStation.callNextVoter();
                        System.out.println("\n[TPOLLCLERK] - Voter called --->\n");
                        setState(PollClerkState.ID_CHECK);
                    }

                    case ID_CHECK -> {
                        System.out.println("\n[TPOLLCLERK] - CASE ID_CHECK --->\n");

                        if (booth.getVotesCount() >= maxVotes) {
                            System.out.println("\n[TPOLLCLERK] - Maximum votes reached --->\n");
                            pollStation.closePS();
                        }

                        if (pollStation.isEmpty()) {
                            System.out.println("\n[TPOLLCLERK] - Polling station is empty --->\n");
                            setState(PollClerkState.INFORMING_EP);
                            break;
                        }
                        System.out.println("\n[TPOLLCLERK] - Checking ID wait --->\n");
                        setState(PollClerkState.ID_CHECK_WAIT);
                    }

                    case INFORMING_EP -> {
                        System.out.println("\n[TPOLLCLERK] - CASE INFORMING_EP --->\n");
                        System.out.println("\n[TPOLLCLERK] - Informing exit poll --->\n");
                        exitPoll.close();
                        System.out.println("\n[TPOLLCLERK] - Exit poll closed --->\n");
                        System.out.println("\n[TPOLLCLERK] - Gathering votes --->\n");
                        booth.gathering();
                        System.out.println("\n[TPOLLCLERK] - Gathering votes finished --->\n");
                        setState(PollClerkState.GATHERING_VOTES);
                    }

                    case GATHERING_VOTES ->{
                        System.out.println("\n[TPOLLCLERK] - CASE GATHERING_VOTES --->\n");
                        System.out.println("\n[TPOLLCLERK] - Publishing election results --->\n");
                        booth.publishElectionResults();
                        System.out.println("\n[TPOLLCLERK] - Election results published --->\n");
                        
                        System.out.println("\n[TPOLLCLERK] - Closing polling station --->\n");
                        pollStation.shutdown();
                        System.out.println("\n[TPOLLCLERK] - Polling station closed --->\n");

                        System.out.println("\n[TPOLLCLERK] - Closing voting booth --->\n");
                        booth.shutdown();
                        System.out.println("\n[TPOLLCLERK] - Voting booth closed --->\n");

                        System.out.println("\n[TPOLLCLERK] - Closing ID check --->\n");
                        idCheck.shutdown();
                        System.out.println("\n[TPOLLCLERK] - ID check closed --->\n");

                        System.out.println("\n[TPOLLCLERK] - Closing exit poll --->\n");
                        exitPoll.shutdown();
                        System.out.println("\n[TPOLLCLERK] - Exit poll closed --->\n");

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
     * The resetInstance method resets the instance of the TPollClerk class to null.
     * So this thread can be reused in the next election.
     */
    private static void resetInstance(){
        instance = null;
    }
}