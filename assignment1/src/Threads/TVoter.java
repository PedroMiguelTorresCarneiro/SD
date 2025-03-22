package Threads;

import Monitors.EvotingBooth.IEVotingBooth_TVoter;
import Monitors.ExitPoll.IExitPoll_TVoter;
import Monitors.IDCheck.IIDCheck_TVoter;
import Monitors.PollStation.IPollStation_TVoter;
import java.util.Random;

/**
 * The TVoter class implements the Runnable interface and represents the life cycle of a voter in the election simulation.
 * The voter's life cycle is modeled as a finite state machine, where the voter can be in one of the following states:
 * WAITING_OUTSIDE, WAITING_INSIDE, ANSWER_SURVEY, CHECKING_ID, EXIT_PS, VOTING, or GO_HOME.
 * The voter interacts with shared regions such as the polling station, ID check, voting booth, and exit poll.
 * This class also provides methods to change the voter's state and generate a new ID.
 *
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public class TVoter implements Runnable {
    // Shared Regions

    /**
     * The pollStation attribute stores a reference to the polling station shared region.
     * This is the interface that the voter uses to interact with the polling station.
     */
    private final IPollStation_TVoter pollStation;

    /**
     * The booth attribute stores a reference to the voting booth shared region.
     * This is the interface that the voter uses to interact with the voting booth.
     */
    private final IEVotingBooth_TVoter booth;

    /**
     * The idCheck attribute stores a reference to the ID check shared region.
     * This is the interface that the voter uses to interact with the ID check.
     */
    private final IIDCheck_TVoter idCheck;

    /**
     * The exitPoll attribute stores a reference to the exit poll shared region.
     * This is the interface that the voter uses to interact with the exit poll.
     */
    private final IExitPoll_TVoter exitPoll;

    /**
     * The state attribute stores the current state of the voter.
     * The initial state is WAITING_OUTSIDE, where the voter waits outside the polling station.
     */
    private VoterState state = VoterState.WAITING_OUTSIDE;

    /**
     * The voterId attribute stores the voter's unique identifier.
     */
    private String voterId;

    /**
     * The random attribute is used to generate random numbers, simulating the voter's behavior in certain situations.
     */
    private final Random random = new Random();

    /**
     * The validID attribute stores the result of the ID check.
     * If the ID is valid, the voter can proceed to the voting booth; otherwise, the voter exits the polling station.
     */
    private boolean validID = true;

    /**
     * The diffIDProb attribute stores the probability of the voter being reborn with a new ID.
     * This is used to simulate voters returning to the polling station with a different identity.
     */
    private final double diffIDProb = 0.6;

    /**
     * The chooseToAnswerProb attribute stores the probability of the voter choosing to answer the exit poll survey.
     */
    private final double chooseToAnswerProb = 0.6;


    private int test = 0;

    /**
     * The VoterState enum defines the possible states of the voter during the simulation.
     */
    public static enum VoterState {
        WAITING_OUTSIDE, 
        WAITING_INSIDE, 
        ANSWER_SURVEY, 
        CHECKING_ID, 
        EXIT_PS, 
        VOTING, 
        GO_HOME 
    }

    /**
     * The TVoter constructor initializes a new TVoter object with the specified attributes.
     *
     * @param voterId     The voter's unique identifier.
     * @param pollStation The polling station shared region.
     * @param idCheck     The ID check shared region.
     * @param booth       The voting booth shared region.
     * @param exitPoll    The exit poll shared region.
     */
    private TVoter(String voterId, IPollStation_TVoter pollStation, IIDCheck_TVoter idCheck, IEVotingBooth_TVoter booth, IExitPoll_TVoter exitPoll) {
        this.voterId = voterId;
        this.pollStation = pollStation;
        this.idCheck = idCheck;
        this.booth = booth;
        this.exitPoll = exitPoll;
    }

    /**
     * The getInstance method returns a new TVoter object by calling the private constructor.
     *
     * @param voterId     The voter's unique identifier.
     * @param pollStation The polling station shared region.
     * @param idCheck     The ID check shared region.
     * @param booth       The voting booth shared region.
     * @param exitPoll    The exit poll shared region.
     * @return A new TVoter object.
     */
    public static Runnable getInstance(String voterId, IPollStation_TVoter pollStation, IIDCheck_TVoter idCheck, IEVotingBooth_TVoter booth, IExitPoll_TVoter exitPoll) {
        return new TVoter(voterId, pollStation, idCheck, booth, exitPoll);
    }

    /**
     * The run method executes the voter's life cycle, which ends when the voter goes home.
     * The voter transitions between states based on interactions with shared regions.
     * This cycle ends when the voter goes home.
     * The voter can be in one of the following states: WATING_OUTSIDE, WATING_INSIDE, ANWSER_SURVEY, CHECKING_ID, EXIT_PS, VOTING, GO_HOME.
     * 
     * WAITING_OUTSIDE: 
     * The voter waits outside the polling station, if the polling station is closed after the election, the voter goes home,
     * otherwise the voter tries to enter in the polling station.
     * 
     * WAITING_INSIDE:
     * The voter waits inside the polling station, to check his ID.
     * 
     * CHECKING_ID:
     * The voter checks his ID, if the ID is invalid, the voter exits the polling station.
     * Otherwise, the voter proceeds to the voting booth.
     * 
     * VOTING:
     * The voter votes, and after voting, the voter exits the polling station.
     * 
     * EXIT_PS:
     * The voter exits the polling station, if the exit poll is closed, the voter goes home.
     * If the voter is chosen to answer the survey and decides to answer, the voter answers the survey.
     * Otherwise, the voter reborns and will wait outside the polling station.
     * 
     * ANWSER_SURVEY:
     * The voter answers the survey (it can lie in its answer) and reborns, waiting outside the polling station.
     *
     * @throws InterruptedException If any thread is interrupted during the simulation.
     */
    @Override
    public void run() {
        try {
            while (state != VoterState.GO_HOME) {
                switch (state) {
                    case WAITING_OUTSIDE:
                        if (pollStation.isCLosedAfterElection()) {
                            setState(VoterState.GO_HOME);
                            break;
                        }

                        pollStation.enterPS(this);
                        break;

                    case WAITING_INSIDE:
                        validID = idCheck.checkID(this);
                        break;

                    case CHECKING_ID:
                        if (!validID) {
                            pollStation.exitingPS(this);
                            break;
                        }

                        booth.vote(this);
                        break;

                    case VOTING:
                        pollStation.exitingPS(this);
                        break;

                    case EXIT_PS:
                        if (!exitPoll.isOpen()) {
                            setState(VoterState.GO_HOME);
                            break;
                        }

                        if (!exitPoll.choosen()) {
                            reborn();
                            break;
                        }
                        
                        if (Math.random() < chooseToAnswerProb) {
                            exitPoll.callForSurvey(booth.getVote(voterId), this);
                        } else {
                            reborn();
                        }

                        break;

                    case ANSWER_SURVEY:
                        reborn();
                        break;

                    default:
                        break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The setState method updates the voter's current state.
     *
     * @param state The new state of the voter.
     */
    public void setState(VoterState state) {
        this.state = state;
    }

    /**
     * The reborn method resets the voter's state to WAITING_OUTSIDE, simulating the voter returning to the polling station.
     * The voter has a probability of being reborn with a new ID, as defined by diffIDProb.
     */
    private void reborn() {
        boolean diffID = Math.random() < diffIDProb;
        if (diffID) {
            generateNewID();
        }

        setState(VoterState.WAITING_OUTSIDE);
    }

    /**
     * The generateNewID method generates a new random ID for the voter.
     * The new ID is a random number between 0 and 64, prefixed with "V".
     */
    private void generateNewID() {
        String newId;
       
        test = test + 10;
        do {
            newId = "V" + random.nextInt(test, 64 + test);
        } while (voterId.equals(newId));

        voterId = newId;
    }

    /**
     * The getID method returns the voter's current ID.
     *
     * @return The voter's ID.
     */
    public String getID() {
        return voterId;
    }
}