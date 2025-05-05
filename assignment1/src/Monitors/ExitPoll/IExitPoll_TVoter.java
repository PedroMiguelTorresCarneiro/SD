package Monitors.ExitPoll;

/**
 * The IExitPoll_TVoter interface contains the methods that the exit poll shared region
 * should implement to interact with the voter threads.
 */
public interface IExitPoll_TVoter {
    /**
     * The choosen method is called by the voter to see if he was choosed for the survey.
     * @return true if the voter was choosen for the survey, false otherwise.
     * @throws InterruptedException if the thread is interrupted
     */
    boolean choosen() throws InterruptedException;

    /**
     * The callForSurvey method is called by the voter to vote in the survey.
     * @param vote The vote of the voter
     * @param voterId The ID of the voter
     * @throws InterruptedException if the thread is interrupted
     */
    void callForSurvey(char vote, String voterId) throws InterruptedException;
    
    /**
     * The isOpen method is called by the voter to see if the exit poll is open.
     * @return true if the exit poll is open, false otherwise.
     */
    boolean isOpen();
}
