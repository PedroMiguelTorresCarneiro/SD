package Monitors.PollStation;

public interface IPollStation {
    
    static IPollStation getInstance(int capacidadeMax){
        return MPollStation.getInstance(capacidadeMax);
    }

    public void openPS(int maxVotes);

    public void callNextVoter();

    public void closePS();

    public boolean open();

    public void enterPS(String voterId);

    public void exitingPS();
    
}
