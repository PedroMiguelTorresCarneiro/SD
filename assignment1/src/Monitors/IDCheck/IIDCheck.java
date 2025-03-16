package Monitors.IDCheck;

import Monitors.IAll;

public interface IIDCheck extends IAll {
    
    static IIDCheck getInstance() {
        return MIDCheck.getInstance();
    }
    
    public boolean verificarID(String voterId);
    boolean idJaUsado(String voterId);
}
