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
   private RoleType roleType = null;
   private String vote = null;
   private String voterId = null;
   private boolean answerType1 = false;
   private char answerType2;
   private int answerType3;


   public static Message getInstance(MessageType type, RoleType role) {
      return new Message(type, role);
   }

   public static Message getInstance(MessageType type, RoleType role, String info) {
      return new Message(type, role, info);
   }

   public static Message getInstance(MessageType type, RoleType role, boolean bool) {
      return new Message(type, role, bool);
   }

   public static Message getInstance(MessageType type, RoleType role, char vote) {
      return new Message(type, role, vote);
   }

   public static Message getInstance(MessageType type, RoleType role, int nVotes) {
      return new Message(type, role, nVotes);
   }

   public static Message getInstance(MessageType type, RoleType role, String info, int nVotes) {
      return new Message(type, role, info, nVotes);
   }

   /* FORM 1 */
   private Message (MessageType type, RoleType role){
         msgType = type;
         roleType = role;
   }

   /* FORM 2 */
   private Message (MessageType type, RoleType role, String info){
         msgType = type;
         roleType = role;
         if(type == MessageType.GET_VOTE){
            vote = info;
         }else {
            voterId = info;
         }
   }

   /* FORM 3 */
   private Message (MessageType type, RoleType role, boolean bool){
      msgType = type;
      roleType = role;
      answerType1 = bool;
   }

   /* FORM 4 */
   private Message (MessageType type, RoleType role, char vote){
      msgType = type;
      roleType = role;
      answerType2 = vote;
   }

   /* FORM 5 */
   private Message (MessageType type, RoleType role, int nVotes){
      msgType = type;
      roleType = role;
      answerType3 = nVotes;
   }

   /* FORM 6 
   *
   *     @param fileName logging file name
   *     @param nIter number of iterations of the customer life cycle
   */
   private Message (MessageType type, RoleType role, String fileName, int nIter){
      msgType = type;
      roleType = role;
      voterId = fileName;
      answerType3 = nIter;
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
    * Getting MessageType.
    *    
    *   @return message type
    */
   public MessageType getMessageType() {
      return msgType;
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

    public RoleType getRoleType() {
        return roleType;
    }

}
