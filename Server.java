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
        int i = 1;
         
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
            ClientHandler mtch = new ClientHandler(s, ""+i, dis, dos, i);
 
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
    static char lastLetter;

    static class ClientHandler implements Runnable {
        Scanner scn = new Scanner(System.in);
        private String name;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;
        private int col;

        //Rule Flags
        boolean gameStart   = false;
        boolean last2First  = false;

       

        // ANSI escape code constants for text colors and background colors
        String RESET = "\u001B[0m";
        String RED_TEXT = "\u001B[31m";
        String GREEN_TEXT = "\u001B[32m";
        String YELLOW_TEXT = "\u001B[33m";
        String BLACK_BG = "\u001B[40m";
        String WHITE_BG = "\u001B[47m";

        public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos, int col){
            this.dis = dis;
            this.dos = dos;
            this.name = name;
            this.s = socket;
            this.col = col;
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
                    Random r = new Random();
                    gameStart = true;
                    lastLetter = (char)(r.nextInt(26)+'A');
                    for(ClientHandler mc : Server.ar){
                        mc.dos.writeUTF("The first letter to use is: " + lastLetter);
                    }
                    continue;
                }else if(input.contains("/NAME")){
                    StringTokenizer newName = new StringTokenizer(input, " ");
                    if(newName.countTokens()>1){
                        newName.nextToken();
                        this.name = newName.nextToken();
                    }
                    continue;
                }else if(input.contains("/GAMEMODE")){
                    StringTokenizer gm = new StringTokenizer(input);
                    if(gm.countTokens()>1){
                        gm.nextToken();
                        if(gm.nextToken().equals("LAST2FIRST")){
                            last2First = true;
                            dos.writeUTF("Last letter will be the first for the next name."); 
                        }
                        
                    }
                    
                }
                if(input.charAt(input.length()-1)==lastLetter){

                }
                // break the string into message
                StringTokenizer st = new StringTokenizer(input, "#");
                String MsgToSend = st.nextToken();
 

                for (ClientHandler mc : Server.ar) 
                {
                    // Write on all output streams
                    // output stream
                    if(mc.name != this.name){
                        String colorcode = "\u001B[3" + this.col + "m";
                        mc.dos.writeUTF(colorcode + this.name + RESET +" : " + MsgToSend);
                    }
                    
                }
                } catch (IOException e) {
                    break;
                    //e.printStackTrace();
                }
            }
        }
    }
}