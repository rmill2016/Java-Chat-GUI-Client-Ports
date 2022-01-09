import java.io.*;
import java.net.*;

public class FTPClient {
	private Socket socket;
	private BufferedReader keyboard;
	private DataInputStream input;
	private DataOutputStream output;

	private final String EXIT_CMD = "exit";
	private final String PUT_CMD = "put";
	private final String GET_CMD = "get";

	public FTPClient(Socket socket) {
	try {
	this.socket = socket;
	keyboard = new BufferedReader (new InputStreamReader(System.in));
	input = new DataInputStream(socket.getInputStream());
	output = new DataOutputStream(socket.getOutputStream());
	} catch (Exception e) {
	System.out.println(e.getMessage());
	}
}

	public void sendFile(String fileName) {
	try {
	// read file
	File file = new File(fileName);
	if (!file.exists()) {
	System.out.println("File (" + fileName + ") does not exist");
	return;
}
	//send file name
	output.writeUTF(fileName);

	//send data of the file
	System.out.println("Sending file (" + fileName + ") ... ");
	FileInputStream inFile = new FileInputStream(file);
	int ch;
	do {
	ch = inFile.read();
	output.writeUTF(String.valueOf(ch));
	} while (ch != -1);
	inFile.close();

	// show message from server
	System.out.println(input.readUTF());

	} catch (Exception e) {
	System.out.println(e.getMessage());
}
}
	/* receive file from server */
	public void receiveFile(String fileName) {
	try {
	
	//send file name and receive status of the file
	output.writeUTF(fileName);
	String msg = input.readUTF();

	if (msg.equals("NOT_FOUND")) {
	System.out.println("no file in server");
	return;
	} else if (msg.equals("READY")) {

	// receive data and write to a file
	File file = new File(fileName);
	FileOutputStream outFile = new FileOutputStream(file);
	int ch;
	String line;
	do {
	line = input.readUTF();
	ch = Integer.parseInt(line);
	if (ch != -1) {
	outFile.write(ch);
	}
   } while (ch != -1);
	outFile.close();

	// show message from server
	System.out.println(input.readUTF());
	}
  } catch (Exception e) {
	System.out.println(e.getMessage());
}
}

	/* run ftp commands in client */
public void run() {
try {

	String cmd = "";
	while (true) {
	// type a command
	System.out.println("ftp> ");
	cmd = keyboard.readLine();
	System.out.println(cmd);

	// exit
	if (cmd.equals(EXIT_CMD)) {
	output.writeUTF(EXIT_CMD);
	return;

	// send file
	} else if (cmd.contains(PUT_CMD)) {
	String str[] = cmd.split(" ");
	if (str.length == 2) {
	String fileName = str[1];
	output.writeUTF(PUT_CMD);
	sendFile(fileName);
	}

	// receive file
	} else if (cmd.contains(GET_CMD)) {
	String str[] = cmd.split(" ");
	if (str.length == 2) {
	String fileName = str[1];
	output.writeUTF(GET_CMD);
	receiveFile(fileName);
	}
}
}
} catch (Exception e) {
	System.out.println(e.getMessage());
}
}

	public static void main(String[] args) throws Exception {

	// check parameters
	if (args.length != 2) {
	System.out.println("Usage: java FTPClient <host> <port>");
	return;
	}

	//create a socket
	String host = args[0];
	int port = Integer.parseInt(args[1]);
	Socket socket = new Socket(host, port);

	// run ftp client
	FTPClient client = new FTPClient(socket);
	client.run();
	}
}
