package commInfra.interfaces.Repository;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRepo_Tvoter extends Remote{
    int getNumberOfVoters() throws RemoteException;
}
