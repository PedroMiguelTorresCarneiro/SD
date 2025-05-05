package clientSide.stubs;

public class Stub {
    protected final String serverHost;
    protected final int serverPort;

    protected Stub(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
    }
}
