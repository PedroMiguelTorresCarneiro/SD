package commInfra.interfaces.MainGUI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMainGUI_ALL extends Remote {

    // Poll Station
    void logPOLLSTATION(String state) throws RemoteException;

    // Waiting Queue (outside)
    void addExternalFIFO(String voterId) throws RemoteException;
    void removeExternalFIFO(String voterId) throws RemoteException;

    // Inside Queue (inside)
    void addInternalFIFO(String voterId) throws RemoteException;
    void removeInternalFIFO(String voterId) throws RemoteException;

    // IDCheck
    void logIDCHECK(String label, char status) throws RemoteException;
    void addIdcheckFIFO(String voterId) throws RemoteException;
    void removeIdcheckFIFO(String voterId) throws RemoteException;

    // Voting
    void logVOTING(String voterId) throws RemoteException;

    // ExitPoll
    void logSURVEY(String voterId) throws RemoteException;

    // Election Survey
    void updatePartyA_survey(int percentage) throws RemoteException;
    void updatePartyB_survey(int percentage) throws RemoteException;
    void setSurveyPartyAwinner() throws RemoteException;
    void setSurveyPartyBwinner() throws RemoteException;
    void setSurveyTie() throws RemoteException;

    // Final Election
    void updatePartyA(int percentage) throws RemoteException;
    void updatePartyB(int percentage) throws RemoteException;
    void setElecPartyAwinner() throws RemoteException;
    void setElecPartyBwinner() throws RemoteException;
    void setElecTie() throws RemoteException;

    // General
    void clean() throws RemoteException;
    void setStartButtonEnabled(boolean enabled) throws RemoteException;
}
