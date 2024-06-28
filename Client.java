import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
// Java implementation for multithreaded chat client - https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
// Save file as Client.java 

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class Client 
{ 
   public static final int SERVER_PORT = 25565;  //some default port
   public String name;
	public static void main(String args[]) throws UnknownHostException, IOException 
	{ 
	
       final String hostName;
       if (args.length > 0) {
          hostName = args[0];
       } else {
          hostName = "localhost";
       }

       final int portNumber;
       if (args.length > 1) {
          portNumber = Integer.parseInt(args[1]);
       } else {
          portNumber = SERVER_PORT;
       }


		Scanner scn = new Scanner(System.in); 
		
		// getting localhost ip 
		InetAddress ip = InetAddress.getByName(hostName); 
		
		// establish the connection 
		Socket s = new Socket(ip, portNumber); 
		
		// obtaining input and out streams 
		DataInputStream dis = new DataInputStream(s.getInputStream()); 
		DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

		// sendMessage thread 
		Thread sendMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 
				while (true) { 

					// read the message to deliver. 
					String msg = scn.nextLine(); 

					try { 
						// write on the output stream 
						dos.writeUTF(msg); 
					} catch (IOException e) { 
						e.printStackTrace(); 
						break;
					} 
				} 
			} 
		}); 
		
		// readMessage thread 
		Thread readMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try { 
						// read the message sent to this client 
						String msg = dis.readUTF(); 
						System.out.println("\u001B[31m" + msg); 
					} catch (IOException e) { 
						break;
						//e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		sendMessage.start(); 
		readMessage.start(); 


	} 
}
