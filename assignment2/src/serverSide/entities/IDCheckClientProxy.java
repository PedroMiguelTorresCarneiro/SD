package serverSide.entities;


import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.IDCheckInterface;


/**
 *
 * @author pedrocarneiro
 */
public class IDCheckClientProxy extends Proxy {
    public IDCheckClientProxy(ServerCom sconi, IDCheckInterface sharedRegionInterface) {
        super("ProxyIDCheck_", sconi );
        this.sharedRegionInterface =  sharedRegionInterface;
    }
    
}
