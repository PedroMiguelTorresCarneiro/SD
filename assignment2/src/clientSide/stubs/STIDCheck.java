package clientSide.stubs;

import commInfra.MessageType;
import commInfra.interfaces.IDCheck.IIDCheck_ALL;

public class STIDCheck extends Stub implements IIDCheck_ALL {
    private static STIDCheck instance = null;

    private STIDCheck(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
    }

    public static STIDCheck getInstance(String serverHostName, int serverPortNumb) {
        if (instance == null) {
            instance = new STIDCheck(serverHostName, serverPortNumb);
        }
        return instance;
    }

    @Override
    public boolean checkID(String voterId) throws InterruptedException {
        return boolComm(MessageType.CHECK_ID, voterId);
    }

}