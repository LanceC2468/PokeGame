import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
     public static final int SERVER_PORT = 25565; // Should be above 1023.

   public static void main (String[] args)
   {
      Socket          socket = null;
      BufferedReader  in = null;
      PrintWriter     out = null;

      final String hostName;
      if (args.length > 0)
      {
         hostName = args[0];
      }
      else
      {
         hostName = "localhost";
      }

      final int portNumber;
      if (args.length > 1)
      {
         portNumber = Integer.parseInt(args[1]);
      }
      else
      {
         portNumber = SERVER_PORT;
      }

      // Get this client's process id number (PID). This helps
      // to identify the client in the server's transcrip.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("CLIENT: Process ID number (PID): " + pid );

      // Make a connection to the server
      try
      {
         System.out.println("CLIENT: connecting to server: " + hostName + " on port " + portNumber );
         socket = new Socket(InetAddress.getByName(hostName), portNumber);

         in = new BufferedReader(
                  new InputStreamReader(
                       socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot connect to server.");
         //System.out.println( e );
         e.printStackTrace();
         System.exit(-1);
      }

      // Implement the appropriate client/server protocol.

       // Send the server multiple requests.
       final Scanner stdin = new Scanner(System.in);
       final int length = stdin.nextInt(); // Should be a non-negative integer.
       out.println(length);                // Send the sequence length to the server.
       for(int j=0; j<length;j++)
       {
          // Send the server one request.
          
          final int seqLength = stdin.nextInt();
          out.println(seqLength);
          for (int i = 0; i < seqLength; ++i)    // Should have length integers to read.
          {
             final int n = stdin.nextInt();
             out.println(n); // Send each int as a text string on its own line.
          }
          out.flush(); // all the ints
 
          // Receive the server's response.
          try
          {
             final String response = in.readLine();
             final int sum = Integer.parseInt(response.trim());
             System.out.println("CLIENT: Server response is: sum = " + sum);
          }
          catch(IOException e)
          {
             System.out.println("CLIENT: Cannot receive response from server.");
             System.out.println( e );
          }
       }
 
       // Disconnect from the server.
       try
       {
          socket.close();
          System.out.println("CLIENT: Closed socket.");
       }
       catch (IOException e)
       {
          System.out.println("CLIENT: Cannot disconnect from server.");
          System.out.println( e );
       }


   }
}
