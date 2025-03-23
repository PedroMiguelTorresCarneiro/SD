package Monitors.ExitPoll;

import Threads.TPollster;

/**
 * The IExitPoll_TPollster interface contains the methods that the exit poll shared region
 * should implement to interact with the pollster thread.
 */

public interface IExitPoll_TPollster {
    /**
     * The isOpen method is called by the pollster to check if the exit poll is open.
     * @return boolean value that indicates if the exit poll is open.
     */
    boolean isOpen();

    /**
     * The conductSurvey method is called by the pollster to conduct the survey to the seleceted voter.
     * @param pollster The pollster thread that is conducting the survey.
     * @throws InterruptedException Exception that may be thrown if the thread is interrupted.
     */
    void conductSurvey(TPollster pollster) throws InterruptedException;

    /**
     * The waitForVoters method is called by the pollster to wait for the voters to arrive at the exit poll.
     * @param pollster The pollster thread that is waiting for the voters.
     */
    void waitForVoters(TPollster pollster);

    /**
     * The publishResults method is called by the pollster to publish the results of the survey.
     * @param pollster The pollster thread that is publishing the results.
     * @throws InterruptedException Exception that may be thrown if the thread is interrupted.
     */
    void publishResults(TPollster pollster) throws InterruptedException;
}
