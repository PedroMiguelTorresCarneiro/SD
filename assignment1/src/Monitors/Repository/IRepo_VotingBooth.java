package Monitors.Repository;


interface IRepo_VotingBooth {
    void logVoting(String voterId, char vote);
    void logElectionResults(long A, long B, String winner);
}
