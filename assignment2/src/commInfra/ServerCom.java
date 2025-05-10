package commInfra;

import java.io.*;
import java.net.*;

public class ServerCom {
    private ServerSocket listeningSocket = null;
    private Socket commSocket = null;
    private int serverPortNumb;

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public ServerCom(int portNumb) {
        serverPortNumb = portNumb;
    }

    public ServerCom(int portNumb, ServerSocket lSocket) {
        serverPortNumb = portNumb;
        listeningSocket = lSocket;
    }

    public void start() {
        try {
            listeningSocket = new ServerSocket(serverPortNumb);
            listeningSocket.setSoTimeout(500000000); // 1s timeout
        } catch (BindException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Port already in use: " + serverPortNumb);
            e.printStackTrace();
            System.exit(1);
        } catch (SocketException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error setting listening timeout!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error establishing connection on port: " + serverPortNumb);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void end() {
        try {
            listeningSocket.close();
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error closing listening socket!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public ServerCom accept() throws SocketTimeoutException {
        ServerCom scon = new ServerCom(serverPortNumb, listeningSocket);
        try {
            scon.commSocket = listeningSocket.accept();
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("Listening timeout!");
        } catch (SocketException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Listening socket was closed during accept!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Could not accept client connection!");
            e.printStackTrace();
            System.exit(1);
        }


        try {
            scon.out = new ObjectOutputStream(scon.commSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Could not open output stream!");
            e.printStackTrace();
            System.exit(1);
        }
        
        try {
            scon.in = new ObjectInputStream(scon.commSocket.getInputStream());
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Could not open input stream!");
            e.printStackTrace();
            System.exit(1);
        }

        

        return scon;
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error closing input stream!");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            out.close();
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error closing output stream!");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            commSocket.close();
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error closing communication socket!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Object readObject() {
        Object fromClient = null;
        try {
            fromClient = in.readObject();
        } catch (InvalidClassException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Object could not be deserialized!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error reading object from input stream!");
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Unknown data type received!");
            e.printStackTrace();
            System.exit(1);
        }
        return fromClient;
    }

    public void writeObject(Object toClient) {
        try {
            out.writeObject(toClient);
            out.flush(); // 🔥 Flush para garantir envio imediato
        } catch (InvalidClassException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Object could not be serialized!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotSerializableException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Object does not implement Serializable!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() +
                " - Error writing object to output stream!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
