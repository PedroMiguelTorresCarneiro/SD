package serverSide.sharedRegions;

import clientSide.stubs.STRepo;

public class PollStation extends SharedRegion {
    public PollStation(STRepo reposStub) {
        super(reposStub);
    }
}
