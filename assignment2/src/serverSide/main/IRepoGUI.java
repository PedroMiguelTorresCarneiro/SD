public interface IRepoGUI {
    void logWaiting(String voterId);
    void logInside(String voterId);
    void logIDCheck(String voterId, char accepted);
    void logVoting(String voterId, char vote);
    void logExitPoll(String voterId);
    void logSurvey(String voterId, char lieOrNot);
    void logSurveyResults(long A, long B, String winner);
    void logElectionResults(long A, long B, String winner);
    void logPollStation(String state);
}
