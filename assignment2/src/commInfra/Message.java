package commInfra;

import java.io.*;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable
{
  /**
   *  Serialization key.
   */

   private static final long serialVersionUID = 2021L;

  /**
   *  Message type.
   */

   private MessageType msgType = null;
   private String vote = null;
   private String voterId = null;
   private boolean answerType1 = false;
   private char answerType2;
   private int answerType3;


   public static Message getInstance(MessageType type) {
      return new Message(type);
   }

   public static Message getInstance(MessageType type, String info) {
      return new Message(type, info);
   }

   public static Message getInstance(MessageType type, boolean bool) {
      return new Message(type, bool);
   }

   public static Message getInstance(MessageType type, char vote) {
      return new Message(type, vote);
   }

   public static Message getInstance(MessageType type, int nVotes) {
      return new Message(type, nVotes);
   }

   /* FORM 1 */
   private Message (MessageType type){
         msgType = type;
   }

   /* FORM 2 */
   private Message (MessageType type, String info){
         msgType = type;
         if(type == MessageType.GET_VOTE){
            vote = info;
         }else {
            voterId = info;
         }
   }

   /* FORM 3 */
   private Message (MessageType type, boolean bool){
      msgType = type;
      answerType1 = bool;
   }

   /* FORM 4 */
   private Message (MessageType type, char vote){
      msgType = type;
      answerType2 = vote;
   }

   /* FORM 5 */
   private Message (MessageType type, int nVotes){
      msgType = type;
      answerType3 = nVotes;
   }
   

   /**
   *  Getting message type.
   *
   *     @return message type
   */
   public MessageType getMsgType ()
   {
      return (msgType);
   }

   /**
    * Getting the vote.
    *    
    *   @return vote
    */
   public String getVote ()
   {
      return (vote);
   }

   /**
    * Getting the voter id.
    *    
    *   @return voter id
    */
   public String getVoterId ()
   {
      return (voterId);
   }


  /**
   *  Printing the values of the internal fields.
   *
   *  It is used for debugging purposes.
   *
   *     @return string containing, in separate lines, the pair field name - field value
   */
   @Override
   public String toString ()
   {
      return ("Message type = " + msgType +
              "\nVoter Id = " + voterId +
              "\nVote = " + vote +
              "\n");
   }

    public boolean getAnswerType1() {
        return answerType1;
    }

    public char getAnswerType2() {
        return answerType2;
    }

    public int getAnswerType3() {
        return answerType3;
    }

}
