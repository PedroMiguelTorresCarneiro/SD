package commInfra.interfaces.ExitPoll;
import java.rmi.*;

/**
 * The IExitPoll_TPollster interface contains the methods that the exit poll shared region
 * should implement to interact with the pollster thread.
 */

public interface IExitPoll_TPollster extends Remote{
    /**
     * The isOpen method is called by the pollster to check if the exit poll is open.
     * @return Boolean value that indicates if the exit poll is open.
     */
    boolean isOpen() throws RemoteException;

    /**
     * The conductSurvey method is called by the pollster to conduct the survey to the seleceted voter.
     * @throws InterruptedException Exception that may be thrown if the thread is interrupted.
     */
    void conductSurvey() throws RemoteException;

    /**
     * The publishResults method is called by the pollster to publish the results of the survey.
     * @throws InterruptedException Exception that may be thrown if the thread is interrupted.
     */
    void publishResults() throws RemoteException;
}
