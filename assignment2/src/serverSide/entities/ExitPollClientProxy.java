package serverSide.entities;

import commInfra.ServerCom;
import serverSide.main.sharedRegions.interfaces.EvotingBoothInterface;

/**
 *
 * @author pedrocarneiro
 */
public class ExitPollClientProxy extends Proxy {
    public ExitPollClientProxy(ServerCom sconi, EvotingBoothInterface sharedRegionInterface) {
        super("ProxyExitPoll_", sconi );
        this.sharedRegionInterface =  sharedRegionInterface;
    }
}
