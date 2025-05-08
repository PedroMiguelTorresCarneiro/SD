package serverSide.entities;

import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.PollStationInterface;

/**
 *
 * @author pedrocarneiro
 */
public class PPollStation extends Proxy {
    public PPollStation(ServerCom sconi, PollStationInterface sharedRegionInterface) {
        super("ProxyPollStation_", sconi);
        this.sharedRegionInterface = sharedRegionInterface;
    } 
}
