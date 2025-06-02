package commInfra.interfaces.Repository;
import java.rmi.*;

/**
 * The IRepo_ExitPoll interface contains the methods that a repository  should implement to interact with the exit poll shared region.
 * The repository shared region interacts with the exit poll shared region to log survey results and publish the winner of the elections.
 * The methods in this interface allow the repository to access the exit poll shared region.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IRepo_ExitPoll extends Remote{
    /**
     * The logSurvey method logs the survey results of a voter in the exit poll shared region.
     * @param voterId The voter's ID.
     * @param lieOrNot  A char that indicates if the voter lied or not in the survey.
     */
    void logSurvey(String voterId, char lieOrNot) throws RemoteException;

    /**
     * The logSurveyResults method logs the survey results of the election in the exit poll shared region.
     * @param A The number of votes for candidate A.
     * @param B The number of votes for candidate B.
     * @param winner The name of the winning candidate.
     */
    void logSurveyResults(long A, long B, String winner) throws RemoteException;
}
