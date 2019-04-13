/*--------------------------------------------------------

1. Name / Date: Olivia Chisman / 4.9.19

2. Java version used, if not the official version for the class:

build 9.0.1+11 

3. Precise command-line compilation examples / instructions:

> javac InetClient.java


4. Precise examples / instructions to run this program:

In separate shell windows:

> java InetClient
> java InetServer

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java InetClient 140.192.1.22
> java InetServer 140.192.1.22

5. List of files needed for running the program.

InetClient.java
InetServer.java

6. Notes:

----------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by oliviachisman on 4/7/19
 */
public class JokeClient {

    private static int port = 4545;

    public static void main(String[] args) {
        String serverName;

        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        System.out.println("Olivia Chisman's Inet client, 1.8.\n");
        System.out.println("Using server: " + serverName + ", port: " + port);

        getJoke(serverName);

    }

    private static void getJoke(String serverName) {
        try {
            // creating a socket with the specified port and server name with which to connect to the server.
            // The port should be the same as that of the server in order to connect to the server
            Socket socket = new Socket(serverName, port);
            // creating a reader to read the data coming into the socket (coming from the server)
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // reading from server
            String textFromServer = fromServer.readLine();
            // printing to console
            System.out.println(textFromServer);

            // closing the connection to the server
            socket.close();
        } catch (IOException e) {
            // catches any exceptions thrown during reading from or writing to the socket
            System.out.println("Socket error");
            e.printStackTrace();
        }
    }
}
