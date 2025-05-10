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
   private String info = null;
   private char caracter;
   private boolean bool = false;
   private int inteiro;
   private long longA, longB;


   public static Message getInstance(MessageType type) {
      return new Message(type);
   }

   public static Message getInstance(MessageType type, String info) {
      return new Message(type , info);
   }

   public static Message getInstance(MessageType type , boolean bool) {
      return new Message(type , bool);
   }

   public static Message getInstance(MessageType type , char vote) {
      return new Message(type, vote);
   }

   public static Message getInstance(MessageType type , int nVotes) {
      return new Message(type, nVotes);
   }

   public static Message getInstance(MessageType type , String info, int nVotes) {
      return new Message(type, info, nVotes);
   }
   
   public static Message getInstance(MessageType type , String info, char vote) {
      return new Message(type, info, vote);
   }
   
   public static Message getInstance(MessageType type,long A, long B, String info) {
      return new Message(type, A, B, info);
   }

   public static Message getInstance(MessageType type,char vote, String info) {
      return new Message(type, vote, info);
   }
   
   /* FORM 1 */
   private Message(MessageType type){
         msgType = type;
   }

   /* FORM 2 */
   private Message(MessageType type , String info){
         msgType = type;
         this.info = info;
   }

   /* FORM 3 */
   private Message(MessageType type , boolean bool){
      msgType = type;
      this.bool = bool;
   }

   /* FORM 4 */
   private Message(MessageType type , char vote){
      msgType = type;
      caracter = vote;
   }

   /* FORM 5 */
   private Message(MessageType type , int nVotes){
      msgType = type;
      inteiro = nVotes;
   }

   /* FORM 6 */
   private Message(MessageType type , String fileName, int nIter){
      msgType = type;
      info = fileName;
      inteiro = nIter;
   }
   
   private Message(MessageType type , String voterId, char vote){
      msgType = type;
      info = voterId;
      caracter = vote;
   }
   
   private Message(MessageType type,long A, long B, String info){
      msgType = type;
      longA = A;
      longB = B;
      this.info = info;
   }

   private Message(MessageType type,char vote, String info){
      msgType = type;
      caracter = vote;
      this.info = info;
   }
   
   public long getLongA(){
       return (longA);
   }
   
   public long getLongB(){
       return (longB);
   }
   
   public char getCaracter(){
       return (caracter);
   }

   public String getInfo(){
       return (info);
   }
   
   public boolean getBool(){
       return (bool);
   }

   public int getInteiro(){
       return (inteiro);
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
              "\nInfo = " + info +
              "\nchar = " + caracter +
              "\nint = " + inteiro +
              "\nbollean = " + bool +
              "\nlong A = " + longA +
              "\nlongB = " + longB +
              "\n");
   }
}
