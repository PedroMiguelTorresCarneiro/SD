package Monitors.ExitPoll;

import Monitors.IAll;

public interface IExitPoll extends IAll{
    
    static IExitPoll getInstance() {
        return MExitPoll.getInstance();
    }
    void registrarSaida(String voterId, String votoReal);
    String entrevistarProximo() throws InterruptedException;
    void anunciarFim();
    boolean isEncerrado();
    public void exibirResultados();
}
