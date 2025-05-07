package serverSide.entities;

import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.RepoInterface;

public class RepoClientProxy extends Proxy {
    public RepoClientProxy(ServerCom sconi, RepoInterface sharedRegionInterface) {
        super("ProxyRepo_", sconi);
        this.sharedRegionInterface = sharedRegionInterface;

    }
}
