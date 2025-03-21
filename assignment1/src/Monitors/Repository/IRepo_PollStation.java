package Monitors.Repository;


public interface IRepo_PollStation {
    void logPollStation(String state);
    void logWaiting(String voterId);
    void logInside(String voterId);
    void logExitPoll(String voterId);
}
