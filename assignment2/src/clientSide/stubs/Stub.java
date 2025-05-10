package clientSide.stubs;

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
        // ✅ Lê a resposta do servidor (ACK ou outro)
        inMessage = (Message) com.readObject();
        com.close();
    }
    
    @SuppressWarnings("static-access")
    protected boolean boolComm(MessageType type){
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
        
        inMessage = (Message) com.readObject();
        com.close();

        return inMessage.getAnswerType1();
    }

    @SuppressWarnings("static-access")
    protected boolean boolComm(MessageType type, String info){
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
        
        inMessage = (Message) com.readObject();
        com.close();

        return inMessage.getAnswerType1();
    }
    
    @SuppressWarnings("static-access")
    protected char charComm(MessageType type, String info){
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
        
        inMessage = (Message) com.readObject();
        com.close();

        return inMessage.getAnswerType2();
    }

    @SuppressWarnings("static-access")
    protected int intComm(MessageType type){
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
        
        inMessage = (Message) com.readObject();
        com.close();

        return inMessage.getAnswerType3();
    }

    @SuppressWarnings("static-access")
    protected int intComm(MessageType type, String info){
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
        
        inMessage = (Message) com.readObject();
        com.close();

        return inMessage.getAnswerType3();
    }

}
