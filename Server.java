import java.net.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.text.Style;
import PokemonData.src.Pokemon;
public class Server {
    static Vector<ClientHandler> ar = new Vector<>();
    static Vector<game> games = new Vector<>();
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

        //  games can last quite long, so to implement and address any issues with joining mid-game, we multi-thread
        //  ____________________________________________________ this loop needs to fork ________________________________________________
        
        while (true) 
        {
            // Accept the incoming request
            System.out.println("awaiting client");
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
    //_______________________________________________________________________________________________________________________________
    /*  after/while forking, implement game in a way that accounts for changing number of players
     *  
     */
}
    //list of gamemode constants

    public static final int LAST2FIRST = 0;
    public static final int TYPES = 1;
    public static final int LAST2FIRST_TYPES = 2;


    //list of type constants
    
    public static final int ALL = 0;
    public static final int NORMAL = 1;
    public static final int FIRE = 2;
    public static final int WATER = 3;
    public static final int ELECTRIC = 4;
    public static final int GRASS = 5;
    public static final int ICE = 6;
    public static final int FIGHTING = 7;
    public static final int POISON = 8;
    public static final int GROUND = 9;
    public static final int FLYING = 10;
    public static final int PSYCHIC = 11;
    public static final int BUG = 12;
    public static final int ROCK = 13;
    public static final int GHOST = 14;
    public static final int DRAGON = 15;
    public static final int DARK = 16;
    public static final int STEEL = 17;
    public static final int FAIRY = 18;
    public static final int NONE = 19;
    
    //other constants
    public static boolean ECHO = true;

    static int gameID = 1141;
    public int[] letterAvail = {45,55,86,61,26,40,66,34,18,8,32,40,79,23,15,65,7,40,135,64,7,25,30,3,6,14};
    LinkedList<String> usedNames = new LinkedList<String>();
    LinkedList<Pokemon> pokemon = new LinkedList<Pokemon>();
    static char lastLetter;

    /*static class Pokemon {
        private String name;
        private int type1;
        private int type2;

        private int type1_alolan;
        private int type2_alolan;

        private int type1_hisuian;
        private int type2_hisuian;

        private int type1_galarian;
        private int type2_galarian;
        
        public Pokemon(String name, int type1, int type2){
            this.name = name;
            this.type1 = type1;
            this.type2 = type2;
        }


        public int getType1(){
            return type1;
        }
        public int getType2(){
            return type2;
        }
    }
*/
    public static boolean fill(ArrayList<Pokemon> emptyList){
        try{
            Scanner dex = new Scanner(new File(".\\PokemonData\\name_types.txt"));
            dex.useDelimiter("\n");
            String name_string = "";
            String types_string;
            final DataOutputStream dos;
            while (dex.hasNext()) {
                String data = dex.next(); // temporary data

                if (data.contains("#"))
                    continue; // throw away Ndex #
                if (data.isBlank())
                    continue; // throw away newline characters
                if (name_string.isEmpty()) { // first string is name entry
                    name_string = data;
                    dex.useDelimiter("#"); // rest of string is types
                    types_string = dex.next();
                    dex.useDelimiter("\n");

                    Pokemon pkmn = new Pokemon(name_string);
                    pkmn.setTypes(types_string.strip());
                    emptyList.add(pkmn);
                    name_string = "";
                    types_string = "";
                }
            }
            dex.close();
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
        return true;
    } 



    static class ClientHandler implements Runnable {
        Scanner scn = new Scanner(System.in);
        private String name;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;
        private int col;
        int ID;   //clientID
        int gID;  //gameID

        //Rule Flags
        boolean gamePause   = true;
        int GAMEMODE = 0;
        int TYPING = 0;
       

        // ANSI escape code constants for text colors and background colors
        
        String RESET = "\u001B[0m";
        /*
        String RED_TEXT = "\u001B[31m";
        String GREEN_TEXT = "\u001B[32m";
        String YELLOW_TEXT = "\u001B[33m";
        String BLACK_BG = "\u001B[40m";
        String WHITE_BG = "\u001B[47m";
        */

        public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos, int col){
            this.dis = dis;
            this.dos = dos;
            this.name = name;
            this.s = socket;
            this.col = (col % 6) + 1;  //cycles between red, green, yellow, blue, magenta, cyan
        }

        public void sendMessage(String send, ClientHandler held) {
            try{
                held.dos.writeUTF(send);
            }catch(IOException e){}
        }

        public void sendMessage(String send, Vector<ClientHandler> held){
            for(ClientHandler mc : held){
                try{
                    mc.dos.writeUTF(send);
                }catch(IOException e){

                }
                
            }
        }

        public void sendMessage(int color, String nm, String send, Vector<ClientHandler> held){
            String colorcode = col +"\"";
            for(ClientHandler mc : held){
                try{
                    mc.dos.writeUTF(colorcode + nm + " : " + send);
                }catch(IOException e){

                }
                
            }
        }

            @Override 
            public void run(){
            String input;

              //turn the file into a memory safe array shenanigans
            ArrayList<Pokemon> plist = new ArrayList<Pokemon>();
            fill(plist);
            Pokemon parray[] = new Pokemon[plist.size()];
            parray = plist.toArray(parray);
            

                while(true){
                    try{
                        // receive the string
                    input = dis.readUTF();
                    input = input.toUpperCase();
                    if(ECHO)System.out.println(input);
                    
                    //cs 
                    
                    if(input.equals("GIVE UP") || input.equals("/QUIT")){
                        this.s.close();
                        break;
                    }else if(input.equals("/START")){
                        Random r = new Random();
                        gamePause = false;
                        lastLetter = (char)(r.nextInt(26)+'A');
                        games.add(new game(gameID,Server.ar));
                        for(game g : games){
                            if(g.isPlayer(this)){
                                for(ClientHandler mc : g.getPlayers()){
                                    mc.gamePause = false;
                                    mc.ID = gameID;
                                }
                                sendMessage("The first letter to use is: " + lastLetter, g.getPlayers());
                                gameID++;
                            }
                            
                        }
                        gameID++;
                        Server.ar.clear();
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
                                GAMEMODE = LAST2FIRST;
                                dos.writeUTF("Last letter will be the first for the next name."); 
                            }
                        }        
                    }else if(input.equals("/TEST")){
                        for(int i = 0; i < plist.size(); i++){
                            System.out.println(i + " " + parray[i].getTypes());
                        }
                    }else if(input.contains("/")){
                        dos.writeUTF("Unrecognized command.  Try /START to begin the game or /HELP for the help menu");
                    }else if(input.equals("/ECHO")){
                        if(ECHO)dos.writeUTF("Disabling echo on server");
                        else dos.writeUTF("Enabling echo on server");
                        ECHO = !ECHO;
                        continue;
                    }else if(input.equals("/HELP")){
                        dos.writeUTF("/NAME\t\tchanges username");
                        dos.writeUTF("/GAMEMODE\t\tchanges gamemode.  default is LAST2FIRST");
                        dos.writeUTF("/START\t\tbegins the game");
                        dos.writeUTF("/TEST\t\tdebugging purposes");
                        dos.writeUTF("/QUIT\t\tterminate the game");
                        dos.writeUTF("/ECHO\t\ttoggle server echoing client input.  default ON");
                    }else if(input.isBlank()) {
                        continue;
                    }
                    
                    // break the string into message
                    StringTokenizer st = new StringTokenizer(input, "#");
                    String MsgToSend = st.nextToken();
                    

                    /*
                    //LANCE what you want to implement is a scenario where (I assume) the server handles multiple games simultaneously.
                    //I will further assume that each client will have access to ONLY 1 game.
                    */

                    //implement full game here
                    if(!gamePause){
                        // make the list of pokemon
                        System.out.println(gamePause);
                        for(game g : games){
                            Vector<ClientHandler> players = g.getPlayers();
                            for(ClientHandler p : players){
                                sendMessage(input, p);
                            }
                        }
                        /*
                        for(game g : games){  //iterate through a bunch of games.  Then, check if the gameID matches "this"????
                            if(g.ID == this.ID){
                                sendMessage(this.col,this.name, MsgToSend, g.getPlayers());
                            }
                        }
                        */
                        if(Arrays.stream(parray).anyMatch(input::equals)) {
                            dos.writeUTF("Good job! " + input + " starts with '" + input.charAt(0) + "'.");
                        }else{sendMessage(this.col,this.name, MsgToSend, ar);}
                        //if(Arrays.asList(parray).contains());

                        //is the input valid?
                        String guess = input;
                        //checkGuess();  //ensure valid input  (i.e. not NULL, no leading or trailing white-space, handle special or escape sequences)
                        //game(guess);   //game is a state machine.  it maintains a list of "used-up" pokemon.  dole out penalty on bad guess.  continue on good guess.

                    }else {
                        sendMessage(this.col,this.name, MsgToSend, ar);
                    }


                    } catch (IOException e) {
                        break;
                        //e.printStackTrace();
                    }
                }
            }

            
    }

    static class game {
        ArrayList<Pokemon> usedPokemonList = new ArrayList<Pokemon>();
        Pokemon currentPokemon;
        Vector<ClientHandler> players;
        int ID;
        int GAMEMODE;
        public game(int ID,Vector<ClientHandler> players){
            this.ID = ID;
            this.players = players;
        }
        public ArrayList<Pokemon> getUsedPokemonList(){
            return this.usedPokemonList;
        }
        public boolean guess(String g) {  
            //the user supplies a guess g.
            //behavior of a valid guess g is dependent on GAMEMODE
            //game then updates usedPokemonList and currentPokemon respectively.

            return true;
        }
        

        public Vector<ClientHandler> getPlayers(){
            return players;
        }

        public int getID(){
            return ID;
        }
        
        public boolean isPlayer(ClientHandler player){
            if(players.contains(player)){
                return true;
            }
            return false;
        }
    }
    
}