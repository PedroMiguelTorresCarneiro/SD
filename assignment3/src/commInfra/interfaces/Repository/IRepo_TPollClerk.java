
package commInfra.interfaces.Repository;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRepo_TPollClerk extends Remote{
    int getMaxVotes() throws RemoteException;
}
