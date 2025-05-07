package serverSide.sharedRegions;

import clientSide.stubs.STRepo;

public class SharedRegion {
    protected STRepo reposStub; 

    public SharedRegion(STRepo reposStub) {
        this.reposStub = reposStub;
    }
}
