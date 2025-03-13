package Monitors.EvotingBooth;

public interface IEvotingBooth {
    void voting(String voterID, String vote);
    boolean isRegistered(String voterID);
}
