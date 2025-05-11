/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverSide.sharedRegions.PollStation;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.interfaces.PollStation.IPollStation_ALL;

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

        switch (inMessage.getMsgType()) {
            case CAN_ENTER_PS -> {
                System.out.println(" \n[PollStation] CASE CAN_ENTER_PS ------>");
                String voterId = inMessage.getInfo();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);
                }

                try {
                    System.out.println("Voter ID: " + voterId + " is trying to enter the Poll Station");
                    boolean entered = canEnterPS(voterId);
                    System.out.println("Voter ID: " + voterId + " can enter the Poll Station: " + entered);
                    outMessage = Message.getInstance(MessageType.CAN_ENTER_PS, entered);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante canEnterPS.", inMessage);
                }
            }
            case PS_IS_CLOSED_AFTER -> {
                System.out.println(" \n[PollStation] CASE PS_IS_CLOSED_AFTER ------>");
                System.out.println("Checking if the Poll Station is closed after the election");
                boolean closed = isPSclosedAfter();
                System.out.println("Poll Station is closed after the election: " + closed);
                outMessage = Message.getInstance(MessageType.PS_IS_CLOSED_AFTER, closed);
            }
            case PS_IS_CLOSED_AFTER_ELECTION -> {
                System.out.println(" \n[PollStation] CASE PS_IS_CLOSED_AFTER_ELECTION ------>");
                System.out.println("Checking if the Poll Station is closed after the election");
                boolean closed = isCLosedAfterElection();
                System.out.println("Poll Station is closed after the election: " + closed);
                outMessage = Message.getInstance(MessageType.PS_IS_CLOSED_AFTER_ELECTION, closed);
            }
            case EXITING_PS -> {
                System.out.println(" \n[[PollStation] CASE EXITING_PS ------>");
                String voterId = inMessage.getInfo();
                if (voterId == null || voterId.isEmpty()) {
                    throw new MessageException("Voter ID inválido ou ausente.", inMessage);
                }

                try {
                    System.out.println("Voter ID: " + voterId + " is exiting the Poll Station");
                    exitingPS(voterId);
                    System.out.println("Voter ID: " + voterId + " has exited the Poll Station");
                    outMessage = Message.getInstance(MessageType.ACK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante exitingPS.", inMessage);
                }
            }
            case OPEN_PS -> {
                System.out.println(" \n[PollStation] CASE OPEN_PS ------>");
                try {
                    System.out.println("Opening the Poll Station");
                    openPS();
                    System.out.println("Poll Station is now open");
                    outMessage = Message.getInstance(MessageType.ACK);
                } catch (InterruptedException e) {
                    throw new MessageException("Thread interrompida durante openPS.", inMessage);
                }
            }
            case CALL_NEXT_VOTER -> {
                System.out.println(" \n[PollStation] CASE CALL_NEXT_VOTER ------>");
                System.out.println("Calling the next voter");
                callNextVoter();
                System.out.println("Next voter has been called");
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case CLOSE_PS -> {
                System.out.println(" \n[PollStation] CASE CLOSE_PS ------>");
                System.out.println("Closing the Poll Station");
                closePS();
                System.out.println("Poll Station is now closed");
                outMessage = Message.getInstance(MessageType.ACK);
            }
            case PS_IS_EMPTY -> {
                System.out.println(" \n[PollStation] CASE PS_IS_EMPTY ------>");
                System.out.println("Checking if the Poll Station is empty");
                boolean empty = isEmpty();
                System.out.println("Poll Station is empty: " + empty);
                outMessage = Message.getInstance(MessageType.PS_IS_EMPTY, empty);
            }
            case SHUTDOWN -> {
                System.out.println("🔻 Pedido de shutdown recebido.");
                outMessage = Message.getInstance(MessageType.ACK);  // responde primeiro!
                new Thread(() -> {
                    try {
                        Thread.sleep(100); // pequeno delay para garantir que o ACK é enviado
                        shutdown(); // encerra depois
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
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

    @Override
    public void shutdown() {
        System.exit(0);
    }
    
}
