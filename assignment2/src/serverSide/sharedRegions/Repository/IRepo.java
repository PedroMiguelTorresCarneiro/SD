package serverSide.sharedRegions.Repository;

import commInfra.interfaces.Repository.IRepo_ALL;

/**
 *
 * @author pedrocarneiro
 */
public class IRepo implements IRepo_ALL{
    private static IRepo instance = null;
    private final MRepo repo;
    private int votesNumber, votersNumber, insideQueueMaxCapacity;
    
    private IRepo(int votesNumber, int votersNumber, int insideQueueMaxCapacity){
        this.votesNumber = votesNumber;
        this.votersNumber = votersNumber;
        this.insideQueueMaxCapacity = insideQueueMaxCapacity;
    }
    
    public static IRepo getInstance(int votesNumber, int votersNumber, int insideQueueMaxCapacity){
        if (instance == null) {
            instance = new MRepo(votesNumber, votersNumber, insideQueueMaxCapacity);
        }
        return instance;
    }
    
    @Override
    public void logVoting(String voterId, char vote) {
        repo.logVoting(voterId, vote);
    }

    @Override
    public void logElectionResults(long A, long B, String winner) {
        repo.logElectionResults(A, B, winner);
    }

    @Override
    public void close() {
        repo.close();
    }

    @Override
    public void logPollStation(String state) {
        repo.logPollStation(state);
    }

    @Override
    public void logWaiting(String voterId) {
        repo.logWaiting(voterId);
    }

    @Override
    public void logInside(String voterId) {
        repo.logInside(voterId);
    }

    @Override
    public void logExitPoll(String voterId) {
        repo.logExitPoll(voterId);
    }

    @Override
    public void logIDCheck(String voterId, char accepted) {
        repo.logIDCheck(voterId, accepted);
    }

    @Override
    public void logSurvey(String voterId, char lieOrNot) {
        repo.logSurvey(voterId, lieOrNot);
    }

    @Override
    public void logSurveyResults(long A, long B, String winner) {
        repo.logSurveyResults(A, B, winner);
    }   
}
