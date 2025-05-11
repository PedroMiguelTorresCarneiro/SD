package serverSide.stubs;

import commInfra.MessageType;

/**
 * Stub para comunicar com a mainGUI através de TCP.
 */
public class STmainGUI extends Stub {
    private static STmainGUI instance = null;

    private STmainGUI(String serverHostName, int serverPortNumb) {
        super(serverHostName, serverPortNumb);
        System.out.println("Server MainGUI: " + serverHostName + ":" + serverPortNumb);
    }

    public static STmainGUI getInstance(String host, int port) {
        instance = new STmainGUI(host, port); // SEM if
        return instance;
    }

    
    
    public void logPOLLSTATION(String state) {
        sendMessage(MessageType.LOGPOLLSTATION, state);
    }

    
    public void logIDCHECK(String voter, char accepted) {
        sendMessage(MessageType.LOGIDCHECK, voter, accepted);
    }

    
    public void logSURVEY(String voter) {
        sendMessage(MessageType.LOGSURVEY, voter);
    }

    
    public void logVOTING(String voter) {
        sendMessage(MessageType.LOGVOTING, voter);
    }

    
    public void addExternalFIFO(String voter) {
        sendMessage(MessageType.ADD_EXT_FIFO, voter);
    }

    
    public void removeExternalFIFO(String voter) {
        sendMessage(MessageType.REMOVE_EXT_FIFO, voter);
    }

    
    public void addInternalFIFO(String voter) {
        sendMessage(MessageType.ADD_INT_FIFO, voter);
    }

    
    public void removeInternalFIFO(String voter) {
        sendMessage(MessageType.REMOVE_INT_FIFO, voter);
    }

    
    public void addIdcheckFIFO(String voter) {
        sendMessage(MessageType.ADD_IDCHECK_FIFO, voter);
    }

    
    public void removeIdcheckFIFO(String voter) {
        sendMessage(MessageType.REMOVE_IDCHECK_FIFO, voter);
    }

    
    public void updatePartyA(int votes) {
        sendMessage(MessageType.UPDATE_PARTY_A, votes);
    }

    
    public void updatePartyB(int votes) {
        sendMessage(MessageType.UPDATE_PARTY_B, votes);
    }

    
    public void updatePartyA_survey(int votes) {
        sendMessage(MessageType.UPDATE_PARTY_A_SURVEY, votes);
    }

    
    public void updatePartyB_survey(int votes) {
        sendMessage(MessageType.UPDATE_PARTY_B_SURVEY, votes);
    }

    
    public void setElecPartyAwinner() {
        sendMessage(MessageType.SET_ELEC_WINNER_A);
    }

    
    public void setElecPartyBwinner() {
        sendMessage(MessageType.SET_ELEC_WINNER_B);
    }

    
    public void setSurveyPartyAwinner() {
        sendMessage(MessageType.SET_SURVEY_WINNER_A);
    }

    
    public void setSurveyPartyBwinner() {
        sendMessage(MessageType.SET_SURVEY_WINNER_B);
    }

    
    public void setElecTie() {
        sendMessage(MessageType.SET_ELEC_TIE);
    }

    
    public void setSurveyTie() {
        sendMessage(MessageType.SET_SURVEY_TIE);
    }

    
    public void clean() {
        sendMessage(MessageType.CLEAN_GUI);
    }

    public void setStartButtonEnabled(boolean b) {
        sendMessage(MessageType.STARTBTN_ON, b);
    }
}
