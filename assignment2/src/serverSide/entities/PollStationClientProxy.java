package serverSide.entities;

import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.PollStationInterface;

/**
 *
 * @author pedrocarneiro
 */
public class PollStationClientProxy extends Proxy {
    public PollStationClientProxy(ServerCom sconi, PollStationInterface sharedRegionInterface) {
        super("ProxyPollStation_", sconi);
        this.sharedRegionInterface = sharedRegionInterface;
    } 
}
