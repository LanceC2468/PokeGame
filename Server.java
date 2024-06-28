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
    public int[] letterAvail = {45,55,86,61,26,40,66,34,18,8,32,40,79,23,15,65,7,40,135,64,7,25,30,3,6,14};
    LinkedList<String> usedNames = new LinkedList<String>();

    static class ClientHandler implements Runnable {
        Scanner scn = new Scanner(System.in);
        private String name;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;

        //Rule Flags
        boolean gameStart   = false;
        boolean last2First  = false;

       

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
                }else if(input.equals("/START")){
                    gameStart = true;
                }else if(input.contains("/NAME")){
                    StringTokenizer newName = new StringTokenizer(input, " ");
                    newName.nextToken();
                    this.name = newName.nextToken();
                    continue;
                }else if(input.equals("/GAMEMODE")){
                    last2First = true;
                    input = "Last letter will be the first for the next name.";
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