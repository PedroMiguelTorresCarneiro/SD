package serverSide.sharedRegions.MainGui;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;

public class ImainGUI {

    private final mainGUI gui;

    public ImainGUI(mainGUI gui) {
        this.gui = gui;
    }

    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMsgType()) {
            case LOGPOLLSTATION -> {
                System.out.println("\nLOGPOLLSTATION --->");
                gui.logPOLLSTATION(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGIDCHECK -> {
                System.out.println("\nLOGIDCHECK --->");
                gui.logIDCHECK(inMessage.getInfo(), inMessage.getCaracter());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGSURVEY -> {
                System.out.println("\nLOGSURVEY --->");
                gui.logSURVEY(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case LOGVOTING -> {
                System.out.println("\nLOGVOTING --->");
                gui.logVOTING(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case ADD_EXT_FIFO -> {
                System.out.println("\nADD_EXT_FIFO --->");
                gui.addExternalFIFO(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case REMOVE_EXT_FIFO -> {
                // System.out.println("\nREMOVE_EXT_FIFO --->");
                System.out.println("\nREMOVE_EXT_FIFO --->");
                gui.removeExternalFIFO(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case ADD_INT_FIFO -> {
                System.out.println("\nADD_INT_FIFO --->");
                gui.addInternalFIFO(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case REMOVE_INT_FIFO -> {
                System.out.println("\nREMOVE_INT_FIFO --->");
                gui.removeInternalFIFO(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case ADD_IDCHECK_FIFO -> {
                System.out.println("\nADD_IDCHECK_FIFO --->");
                gui.addIdcheckFIFO(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case REMOVE_IDCHECK_FIFO -> {
                System.out.println("\nREMOVE_IDCHECK_FIFO --->");
                gui.removeIdcheckFIFO(inMessage.getInfo());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case UPDATE_PARTY_A -> {
                System.out.println("\nUPDATE_PARTY_A --->");
                gui.updatePartyA(inMessage.getInteiro());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case UPDATE_PARTY_B -> {
                System.out.println("\nUPDATE_PARTY_B --->");
                gui.updatePartyB(inMessage.getInteiro());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case UPDATE_PARTY_A_SURVEY -> {
                System.out.println("\nUPDATE_PARTY_A_SURVEY --->");
                gui.updatePartyA_survey(inMessage.getInteiro());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case UPDATE_PARTY_B_SURVEY -> {
                System.out.println("\nUPDATE_PARTY_B_SURVEY --->");
                gui.updatePartyB_survey(inMessage.getInteiro());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case SET_ELEC_WINNER_A -> {
                System.out.println("\nSET_ELEC_WINNER_A --->");
                gui.setElecPartyAwinner();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case SET_ELEC_WINNER_B -> {
                System.out.println("\nSET_ELEC_WINNER_B --->");
                gui.setElecPartyBwinner();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case SET_SURVEY_WINNER_A -> {
                System.out.println("\nSET_SURVEY_WINNER_A --->");
                gui.setSurveyPartyAwinner();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case SET_SURVEY_WINNER_B -> {
                System.out.println("\nSET_SURVEY_WINNER_B --->");
                gui.setSurveyPartyBwinner();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case SET_ELEC_TIE -> {
                System.out.println("\nSET_ELEC_TIE --->");
                gui.setElecTie();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case SET_SURVEY_TIE -> {
                System.out.println("\nSET_SURVEY_TIE --->");
                gui.setSurveyTie();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case STARTBTN_ON -> {
                System.out.println("\nSTARTBTN_ON --->");
                gui.setStartButtonEnabled(inMessage.getBool());
                System.out.println("Start button enabled: " + inMessage.getBool());
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case CLEAN_GUI -> {
                System.out.println("\nCLEAN_GUI --->");
                gui.clean();
                outMessage = Message.getInstance(MessageType.ACK);
            }
            default -> throw new MessageException("Mensagem desconhecida no ImainGUI", inMessage);
        }

        return outMessage;
    }
}
