package commInfra.interfaces.Repository;
import java.rmi.*;

/**
 * The IRepo_VotingBooth interface contains the methods that a repository should implement to interact with the voting booth shared region.
 * The repository shared region interacts with the voting booth shared region to log the votes of the voters and the results of the election.
 * The methods in this interface allow the repository to access the voting booth shared region.
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IRepo_VotingBooth extends Remote{
    /**
     * The logVoting method logs the vote of a voter in the voting booth shared region.
     * @param voterId The voter's ID.
     * @param vote The candidate the voter voted for.
     */
    void logVoting(String voterId, char vote) throws RemoteException;

    /**
     * The logElectionResults method logs the results of the election in the voting booth shared region.
     * @param A The number of votes for party A.
     * @param B The number of votes for party B.
     * @param winner The winner of the election.
     */
    void logElectionResults(long A, long B, String winner) throws RemoteException;

    /**
     * The close method closes the repository shared region after the election is over. 
     */
    void close() throws RemoteException;
}
