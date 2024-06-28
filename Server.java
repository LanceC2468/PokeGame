import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.text.Style;

public class Server {
    static Vector<ClientHandler> ar = new Vector<>();
        public static void main(String[] args) throws IOException {
            final int portNumber;
        if (args.length > 0)
        {
            portNumber = Integer.parseInt(args[0]);
        }
        else
        {
            portNumber = 25565;
        }
        ServerSocket ss = new ServerSocket(portNumber);
         
        Socket s;
        int i = 0;
         
        // running infinite loop for getting
        // client request
        while (true) 
        {
            // Accept the incoming request
            s = ss.accept();
 
            System.out.println("New client request received : " + s);
             
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
             
            System.out.println("Creating a new handler for this client...");
 
            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s, ""+i, dis, dos);
 
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
             
            System.out.println("Adding this client to active client list");
 
            // add this client to active clients list
            ar.add(mtch);
 
            // start the thread.
            t.start();
 
            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;
 
    }
        }
    static class ClientHandler implements Runnable {
        Scanner scn = new Scanner(System.in);
        private String name;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;
        boolean isloggedin;
        
        public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos){
            this.dis = dis;
            this.dos = dos;
            this.name = name;
            this.s = socket;
        }
        @Override
        public void run(){
            String input;
            while(true){
                try{
                      // receive the string
                input = dis.readUTF();
                 input = input.toUpperCase();
                System.out.println(input);
                 
                if(input.equals("GIVE UP")){
                    this.s.close();
                    break;
                }
                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(input, "#");
                String MsgToSend = st.nextToken();
                //String recipient = st.nextToken();
 
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (ClientHandler mc : Server.ar) 
                {
                    // Write on all output streams
                    // output stream
                    if(mc.name != this.name){
                        mc.dos.writeUTF(this.name+" : "+MsgToSend);
                    }
                    
                }
                } catch (IOException e) {
                 
                    e.printStackTrace();
                }
            }
        }
    }
}