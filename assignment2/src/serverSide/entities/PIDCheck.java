package serverSide.entities;


import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.IDCheckInterface;


/**
 *
 * @author pedrocarneiro
 */
public class PIDCheck extends Proxy {
    public PIDCheck(ServerCom sconi, IDCheckInterface sharedRegionInterface) {
        super("ProxyIDCheck_", sconi );
        this.sharedRegionInterface =  sharedRegionInterface;
    }
    
}
