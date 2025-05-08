/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverSide.sharedRegions.PollStation;

import commInfra.interfaces.PollStation.IPollStation_ALL;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;

/**
 *
 * @author pedrocarneiro
 */
public class IPollStation implements IPollStation_ALL{
    private static IPollStation instance = null;
    private final MPollStation pollStation;
    
    private IPollStation(MPollStation pollStation){
        this.pollStation = pollStation;
    }
    
    public static IPollStation getInstance(MPollStation pollStation){
        if (instance == null) {
            instance = new IPollStation(pollStation);
        }
        return instance;
    }
    
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getMessageType()) {
            case CAN_ENTER_PS -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);
                }

                try {
                    boolean entered = canEnterPS(voterId);
                    outMessage = Message.getInstance(MessageType.CAN_ENTER_PS, entered);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante canEnterPS.", inMessage);
                }
            }
            case PS_IS_CLOSED_AFTER -> {
                boolean closed = isCLosedAfterElection();
                outMessage = Message.getInstance(MessageType.PS_IS_CLOSED_AFTER, closed);
            }
            case EXITING_PS -> {
                String voterId = inMessage.getVoterId();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);
                }

                try {
                    exitingPS(voterId);
                    outMessage = Message.getInstance(MessageType.EXITING_PS);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante exitingPS.", inMessage);
                }
            }
            case OPEN_PS -> {
                try {
                    openPS();
                    outMessage = Message.getInstance(MessageType.OPEN_PS);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante openPS.", inMessage);
                }
            }
            case CALL_NEXT_VOTER -> {
                callNextVoter();
                outMessage = Message.getInstance(MessageType.CALL_NEXT_VOTER);
            }
            case CLOSE_PS -> {
                closePS();
                outMessage = Message.getInstance(MessageType.CLOSE_PS);
            }
            case PS_IS_EMPTY -> {
                boolean empty = isEmpty();
                outMessage = Message.getInstance(MessageType.PS_IS_EMPTY, empty);
            }

            default -> throw new MessageException("Tipo de mensagem inválido", inMessage);
        }

        return outMessage;
    }
    
    @Override
    public boolean isCLosedAfterElection() {
        return pollStation.isCLosedAfterElection();
    }

    @Override
    public boolean canEnterPS(String voterId) throws InterruptedException {
        return pollStation.canEnterPS(voterId);
    }

    @Override
    public void exitingPS(String voterId) throws InterruptedException {
        pollStation.exitingPS(voterId);
    }

    @Override
    public void openPS() throws InterruptedException {
        pollStation.openPS();
    }

    @Override
    public void callNextVoter() {
        pollStation.callNextVoter();
    }

    @Override
    public void closePS() {
        pollStation.closePS();
    }

    @Override
    public boolean isEmpty() {
        return pollStation.isEmpty();
    }

    @Override
    public boolean isPSclosedAfter() {
        return pollStation.isPSclosedAfter();
    }
    
}
