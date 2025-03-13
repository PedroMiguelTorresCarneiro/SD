package Threads.PollClerk;

public enum ClerkState {
    OPEN_PS,
    WAIT_FOR_VOTERS,
    CHECK_ID,
    CLOSE_PS,
    GATHER_VOTES,
    GO_HOME
}
