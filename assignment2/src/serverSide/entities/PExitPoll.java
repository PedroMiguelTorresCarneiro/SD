package serverSide.entities;

import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.ExitPollInterFace;

/**
 *
 * @author pedrocarneiro
 */
public class PExitPoll extends Proxy {
    public PExitPoll(ServerCom sconi, ExitPollInterFace sharedRegionInterface) {
        super("ProxyExitPoll_", sconi );
        this.sharedRegionInterface =  sharedRegionInterface;
    }
}
