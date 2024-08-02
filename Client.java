import java.util.Scanner;


import javax.swing.*;
import java.awt.Dimension;
// Java implementation for multithreaded chat client - https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
// Save file as Client.java 
import java.awt.event.*;
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class Client implements ActionListener
{ 
	static DataOutputStream dos;
	JFrame jf = new JFrame("Pokegame");
	JScrollPane jsp;
	static JTextArea jt;
	static JTextField jtf;

   public static final int SERVER_PORT = 25565;  //some default port
   public String name;

	public Client(){
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(400, 400);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		JPanel Top = new JPanel();
		Top.setLayout(new BoxLayout(Top, BoxLayout.X_AXIS));
		JPanel Bot = new JPanel();
		Bot.setLayout(new BoxLayout(Bot, BoxLayout.X_AXIS));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		jt = new JTextArea();
		jtf = new JTextField();
		jsp = new JScrollPane(jt);
		jtf.setMinimumSize(new Dimension(200,20));
		jtf.setMaximumSize(new Dimension(400,20));
		JButton snd = new JButton("Send Message");

		snd.addActionListener(this);

		content.add(Box.createVerticalStrut(30));
		Top.add(Box.createHorizontalStrut(10));
		Top.add(jsp);
		Top.add(Box.createHorizontalStrut(10));
		content.add(Top);
		content.add(Box.createVerticalStrut(30));

		
		textPanel.add(jtf);

		content.add(textPanel);
		content.add(Box.createVerticalStrut(30));

		Bot.add(Box.createHorizontalGlue());
		Bot.add(snd);
		Bot.add(Box.createHorizontalGlue());
		content.add(Bot);
		content.add(Box.createVerticalStrut(30));
		
		


		jf.add(content);
		jf.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand() == "Send Message"){
			sendMessage(jtf.getText());
        }
	}
	// sendMessage thread 
	void sendMessage(String msg) 
	{ 
		try { 
			// write on the output stream 
			dos.writeUTF(msg); 
			jtf.setText("");
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}; 
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
		dos = new DataOutputStream(s.getOutputStream()); 
		javax.swing.SwingUtilities.invokeLater(
         () -> new Client()
      );

		
		
		// readMessage thread 
		Thread readMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try { 
						// read the message sent to this client 
						String msg = dis.readUTF(); 
						jt.setText(jt.getText()+msg+"\n");
					} catch (IOException e) { 
						break;
						//e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		//sendMessage.start(); 
		readMessage.start(); 


	} 
}
