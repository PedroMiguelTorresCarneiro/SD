package serverSide.sharedRegions;

import clientSide.stubs.STRepo;

public class Repo extends SharedRegion {
    public Repo(STRepo reposStub) {
        super(reposStub);
    }
}
