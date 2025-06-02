package commInfra.interfaces.Repository;

/**
 * The IRepo_ALL interface contains the methods that a repository should implement to interact with all shared regions.
 * This interface extends the IRepo_VotingBooth, IRepo_PollStation, IRepo_IDChek, and IRepo_ExitPoll interfaces.
 * The repository shared region interacts with the voting booth, polling station, ID check, and exit poll shared regions.
 * The methods in this interface allow the repository to access the shared regions and retrieve information from them.
 * 
 * @see IRepo_VotingBooth
 * @see IRepo_PollStation
 * @see IRepo_IDChek
 * @see IRepo_ExitPoll
 * 
 * @author David Palricas
 * @author Inês Águia
 * @author Pedro Carneiro
 */
public interface IRepo_ALL extends IRepo_VotingBooth, IRepo_PollStation, IRepo_IDChek, IRepo_ExitPoll, IRepo_Tvoter, IRepo_TPollClerk{ 
}
