package serverSide.entities;

import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.ExitPollInterFace;

/**
 *
 * @author pedrocarneiro
 */
public class ExitPollClientProxy extends Proxy {
    public ExitPollClientProxy(ServerCom sconi, ExitPollInterFace sharedRegionInterface) {
        super("ProxyExitPoll_", sconi );
        this.sharedRegionInterface =  sharedRegionInterface;
    }
}
