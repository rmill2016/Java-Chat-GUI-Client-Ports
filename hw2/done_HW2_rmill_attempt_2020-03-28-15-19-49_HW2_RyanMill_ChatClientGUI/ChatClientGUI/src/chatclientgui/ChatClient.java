package chatclientgui;

import java.net.*;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.*;

public class ChatClient {
   private Socket socket              = null;
   private Thread thread              = null;
   private DataInputStream  console   = null;
   private DataOutputStream streamOut = null;
   private ChatClientThread client    = null;
   private String displayName;
   private final String DELIMITER = ":";
   private String prefix;
   private JTextArea taMsg;
   private JTextField tfMsg;
   
   public void showMessage(String msg) {
	   	System.out.println(msg);
	   	taMsg.append(msg + "\n");
   }

   public ChatClient(String serverName, int serverPort, String displayName, JTextArea taMsg,
		   JTextField tfMsg) {
	   
	  this.taMsg = taMsg;
	  this.tfMsg = tfMsg;
	  showMessage("Establishing connection. Please wait ...");
      try
      {  socket = new Socket(serverName, serverPort);
         this.displayName = displayName;
      	 showMessage("Connected: " + socket);
         open();
         prefix = displayName + DELIMITER;
      }
      catch(UnknownHostException uhe)
      {  showMessage("Host unknown: " + uhe.getMessage()); }
      catch(IOException ioe)
      {  showMessage("Unexpected exception: " + ioe.getMessage()); }
   }
   public void send() {
	   try {
		   streamOut.writeUTF(prefix + tfMsg.getText());
		   streamOut.flush();
		   tfMsg.setText("");
	   } catch (Exception e) {
		   showMessage("error: send");
		   close();
	   }
   }
   public void close() {
	   try {
		   if (streamOut != null) streamOut.close();
		   if (socket != null) socket.close();
	   } catch (Exception e) {
		   showMessage("error: close");
		   client.close();
		   client.stop();
	   }
   }
   public void handle(String recvMsg) {
	   // get displayName and input
	   String[] tokens = recvMsg.split(DELIMITER);
	   String displayName = tokens[0];
	   String msg = tokens[1];
	   
	   if (msg.equals(".bye")) {
		   showMessage("Good bye.");
		   close();
	   } else {
		   showMessage(recvMsg);
	   }
   }
   public void open() {
	   try {
		   streamOut = new DataOutputStream(socket.getOutputStream());
		   client = new ChatClientThread(this, socket);
	   } catch (Exception e) {
		   showMessage("error: open");
	   }
   }
   public void stop()
   {  if (thread != null)
      {  thread.stop();  
         thread = null;
      }
      try
      {  if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ..."); }
      client.close();  
      client.stop();
   }
}