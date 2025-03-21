package Monitors.Repository;


public interface IRepo_ExitPoll {
    void logSurvey(String voterId, char lieOrNot);
    void logSurveyResults(long A, long B, String winner);
}
