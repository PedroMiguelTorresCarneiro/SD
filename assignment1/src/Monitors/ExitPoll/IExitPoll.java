package Monitors.ExitPoll;

public interface IExitPoll {
    void addToExitPoll(String voterID);
    String getNextVoter();
    boolean hasVoters();
    void announceElectionEnd(); // 🔹 Avisar o Pollster que as eleições acabaram
    boolean isElectionOver(); // 🔹 O Voter pode saber se ainda pode renascer?
}
