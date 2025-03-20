package Monitors.Logs;

import Monitors.IAll;

public interface ILogs extends IAll {
    
    static ILogs getInstance(int votesNumber, int votersNumber, int fifoLimit){
        return MLogs.getInstance(votesNumber, votersNumber, fifoLimit);
    }
    void logHeader();
    void logPollStation(String state);
    void logWaiting(String voterId);
    void logInside(String voterId);
    void logIDCheck(String voterId, char accepted);
    void logVoting(String voterId, char vote);
    void logExitPoll(String voterId);
    void logSurvey(String voterId, char lieOrNot);
    void logSurveyResults(long A, long B, String winner);
    void logElectionResults(long A, long B, String winner);
}