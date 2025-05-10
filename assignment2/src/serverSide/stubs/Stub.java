package serverSide.stubs;

import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;


public class Stub {
    protected final String serverHostName;
    protected final int serverPortNumb;

    protected Stub(String host, int port) {
        this.serverHostName = host;
        this.serverPortNumb = port;
    }

    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type) {
        ClientCom com;                                                 
        Message outMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type);
        
        com.writeObject(outMessage);
        com.close();
    }

    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type, String info) {
        ClientCom com;                                                 
        Message outMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, info);
        
        com.writeObject(outMessage);
        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type, String info, char vote) {
        ClientCom com;                                                 
        Message outMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, info, vote);
        
        com.writeObject(outMessage);
        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type,long A, long B, String info) {
        ClientCom com;                                                 
        Message outMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, A, B, info);
        
        com.writeObject(outMessage);
        com.close();
    }
}
