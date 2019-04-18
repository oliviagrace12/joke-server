/*--------------------------------------------------------

1. Name / Date: Olivia Chisman / 4.17.19

2. Java version used, if not the official version for the class:

build 9.0.1+11

3. Precise command-line compilation examples / instructions:

> javac JokeServer.java


4. Precise examples / instructions to run this program:

In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.

JokeServer.java
JokeClient.java
JokeClientAdmin.java

6. Notes:

----------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by oliviachisman on 4/13/19
 */
public class JokeClientAdmin {

    private static final int PORT = 5050;

    public static void main(String[] args) {
        String serverName;

        // connects to localhost unless an server IP address is specified in the program arguments
        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        System.out.println("Olivia Chisman's Inet client admin, 1.8.\n");
        System.out.println("Using server: " + serverName + ", PORT: " + PORT);

        System.out.println("Press enter to change server modes or type quit to exit");

        try {
            // Creating reader to read data input by user from keyboard
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // read line from user.
            String line = reader.readLine();
            // if user just presses enter or types anything other than quit,
            // the admin will send a message to the joke server to switch modes
            while (line == null || !line.equalsIgnoreCase("quit")) {
                // creating a socket with the specified PORT and server name with which to connect to the server.
                // The PORT should be the same as that of the server in order to connect to the server
                // Each time the admin connects to the server, the server will switch modes.
                Socket socket = new Socket(serverName, PORT);
                // creating reader to read server output from socket
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // reading output from socket and printing to console
                System.out.println(fromServer.readLine());
                // closing the connection to the server
                socket.close();

                // read next line from user from console
                line = reader.readLine();
            }

        } catch (IOException e) {
            // catches any exceptions thrown during reading from or writing to the socket
            System.out.println("Socket error");
            e.printStackTrace();
        }
    }
}
