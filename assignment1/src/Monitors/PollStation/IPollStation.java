package Monitors.PollStation;

public interface IPollStation {
    void enterPS(String voterID) throws InterruptedException;
    void exitPS(String voterID);
    void closeStation();
    void openStation();
    String getPollState();
    boolean openFifo();
}
