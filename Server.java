import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.text.Style;

import java.io.*;
public class Server {
        public static void main(String[] args) {
            final int portNumber;
        if (args.length > 0)
        {
            portNumber = Integer.parseInt(args[0]);
        }
        else
        {
            portNumber = 25565;
        }

        int clientCounter = 0;

        // Get this server's process id number (PID). This helps
        // to identify the server in TaskManager or TCPView.
        final ProcessHandle handle = ProcessHandle.current();
        final long pid = handle.pid();
        System.out.println("SERVER: Process ID number (PID): " + pid );

        // Get the name and IP address of the local host and
        // print them on the console for information purposes.
        try
        {
            final InetAddress address = InetAddress.getLocalHost();
            System.out.println("SERVER Hostname: " + address.getCanonicalHostName() );
            System.out.println("SERVER IP address: " +address.getHostAddress() );
            System.out.println("SERVER Using port no. " + portNumber);
        }
        catch (UnknownHostException e)
        {
            System.out.println("Unable to determine this host's address.");
            System.out.println( e );
        }

        // Create the server's listening socket.
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("SERVER online:");
        }
        catch (IOException e)
        {
            System.out.println("SERVER: Error creating network connection.");
            //System.out.println( e );
            e.printStackTrace();
            System.exit(-1);
        }

        while(true) // Serve multiple clients.
        {           // Each client can make one request.
            Socket socket = null;
            BufferedReader in = null;
            PrintWriter out = null;

            // Wait for an incoming client request.
            try
            {
                socket = serverSocket.accept();

                // At this point, a client connection has been made.
                in = new BufferedReader(
                        new InputStreamReader(
                            socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream());
            }
            catch(IOException e)
            {
                System.out.println("SERVER: Error connecting to client");
                System.out.println( e );
            }

            ++clientCounter;
            // Get the IP address of the client and log it to the console.
            final InetAddress clientIP = socket.getInetAddress();
            System.out.println("SERVER: Client " + clientCounter + ": IP: " +  clientIP.getHostAddress());
            try{
                socket.close();
            }
            catch(IOException e)
            {
                System.out.println("SERVER: Error communicating with client (Client no. " + clientCounter + ")");
                    System.out.println( e );
            }
        }
    }
}
