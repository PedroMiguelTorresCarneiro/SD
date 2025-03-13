package Threads.Voter;

public enum VoterState {
    WAITING,
    WAITING_FIFO,
    CHECK_ID,
    E_VOTING,
    EXIT_POLL,
    GO_HOME
}
