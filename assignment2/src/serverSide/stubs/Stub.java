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
        Message outMessage, inMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type);
        
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject(); // ✅ Aguarda resposta

        if (inMessage.getMsgType() != MessageType.ACK) {
            System.err.println("Erro: resposta inesperada do servidor.");
            System.exit(1);
        }

        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type, int inteiro) {
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, inteiro);
        
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject(); // ✅ Aguarda resposta

        if (inMessage.getMsgType() != MessageType.ACK) {
            System.err.println("Erro: resposta inesperada do servidor.");
            System.exit(1);
        }

        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type, boolean bool) {
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, bool);
        
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject(); // ✅ Aguarda resposta

        if (inMessage.getMsgType() != MessageType.ACK) {
            System.err.println("Erro: resposta inesperada do servidor.");
            System.exit(1);
        }

        com.close();
    }

    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type, String info) {
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, info);
        
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject(); // ✅ Aguarda resposta

        if (inMessage.getMsgType() != MessageType.ACK) {
            System.err.println("Erro: resposta inesperada do servidor.");
            System.exit(1);
        }

        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type, String info, char vote) {
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, info, vote);
        
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject(); // ✅ Aguarda resposta

        if (inMessage.getMsgType() != MessageType.ACK) {
            System.err.println("Erro: resposta inesperada do servidor.");
            System.exit(1);
        }

        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected void sendMessage(MessageType type,long A, long B, String info) {
        ClientCom com;                                                 
        Message outMessage, inMessage; 

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = Message.getInstance(type, A, B, info);
        
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject(); // ✅ Aguarda resposta

        if (inMessage.getMsgType() != MessageType.ACK) {
            System.err.println("Erro: resposta inesperada do servidor.");
            System.exit(1);
        }

        com.close();
    }
}
