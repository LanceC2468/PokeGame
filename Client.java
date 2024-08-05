import java.util.Scanner;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import java.awt.Dimension;
// Java implementation for multithreaded chat client - https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
// Save file as Client.java 
import java.awt.event.*;
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class Client implements ActionListener, KeyListener
{ 
	static DataOutputStream dos;
	static DataInputStream dis;
	JFrame jf = new JFrame("Pokegame");
	JScrollPane jsp;
	static JTextPane jt;
	static JTextField jtf;
	static StyledDocument doc;
	static Style style;
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

		jt = new JTextPane();
		jt.setEditable(false);
		doc = jt.getStyledDocument();
		jt.setDocument(doc);
		style = jt.addStyle("name style", null);

		jtf = new JTextField();
		jsp = new JScrollPane(jt);
		jtf.setMinimumSize(new Dimension(200,20));
		jtf.setMaximumSize(new Dimension(400,20));
		jtf.addKeyListener(this);
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

		

		//sendMessage.start(); 
		readMessage.start(); 
	}
	public void keyTyped(KeyEvent e){

	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 10){
			sendMessage(jtf.getText());
		}
	}
	public void keyReleased(KeyEvent e) {

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
			if(msg != ""){
				dos.writeUTF(msg); 
				jtf.setText("");
			}
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
		dis = new DataInputStream(s.getInputStream()); 
		dos = new DataOutputStream(s.getOutputStream()); 
		javax.swing.SwingUtilities.invokeLater(
         () -> new Client()
      );

		
		
		


	} 
	// readMessage thread 
	Thread readMessage = new Thread(new Runnable() 
	{ 
		@Override
		public void run() { 

			while (true) { 
				WriteMessage();
				/*try { 
					StyleConstants.setForeground(style, Color.black);
					// read the message sent to this client 
					try{
						doc.insertString(doc.getLength(),dis.readUTF()+"\n", style);
					}catch(IOException x){
						break;
					}
					
				} catch (BadLocationException e ) { 
					break;
					//e.printStackTrace(); 
				} */
			} 
		} 
	}); 
	Color[] col = {Color.BLACK,Color.BLUE,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.orange,Color.yellow,Color.RED.darker()};
	void WriteMessage(){
		try{
			String msg = dis.readUTF();
			try { 
			StyleConstants.setForeground(style, col[Integer.parseInt(msg.substring(0,msg.indexOf("\"")))]);
			// read the message sent to this client 
				doc.insertString(doc.getLength(),msg.substring(msg.indexOf("\"")+1,msg.indexOf(": ")), style);
				StyleConstants.setForeground(style, Color.black);
				doc.insertString(doc.getLength(),msg.substring(msg.indexOf(": "))+"\n", style);
			} catch (BadLocationException e ) { 
				//e.printStackTrace(); 
			} 
			StyleConstants.setForeground(style, Color.black);
		}catch(IOException x){

		}
		
	}
}
