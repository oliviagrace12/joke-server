/*--------------------------------------------------------

1. Name / Date: Olivia Chisman / 4.9.19

2. Java version used, if not the official version for the class:

build 9.0.1+11 

3. Precise command-line compilation examples / instructions:

> javac InetServer.java


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

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class JokeServer {

    private static boolean jokeMode;
    private final static String JOKE_MODE = "joke mode";
    private final static String PROVERB_MODE = "proverb mode";

    public static void main(String[] args) throws IOException {
        int qLen = 6;
        // defining the port on which to start the server socket
        int clientPort = 4545;
        int adminPort = 5050;

        // setting to joke mode initially
        jokeMode = true;

        // creating a server socket for clients to connect to
        ServerSocket serverSocketForClients = new ServerSocket(clientPort, qLen);
        // creating a server socket for admin to connect to
        ServerSocket serverSocketForAdmin = new ServerSocket(adminPort, qLen);

        // letting the user know that the server has started, and on which port
        System.out.println("Olivia Chisman's Inet server 1.8 starting up in " + getMode() + " mode, listening to port " + clientPort + ".\n");

        // program runs in infinite loop, unless exception is thrown
        while (true) {
            serverSocketForAdmin.accept();
            jokeMode = !jokeMode;
            System.out.println("Server now in " + getMode());

            // if any clients try to connect, the serverSocket will accept their connections and return a socket
//            Socket clientSocket = serverSocketForClients.accept();
            // this socket is passed to a new thread that is spawned (an instance of the Worker class) and start
            // is called on this thread, kicking off the processes in the Worker class run method.
//            new Worker(clientSocket).start();
        }
    }

    private static String getMode() {
        return jokeMode ? JOKE_MODE : PROVERB_MODE;
    }
}

class Worker extends Thread {

    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // creating a reader to read the data coming in to the socket from the client
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // creating a stream to write to the socket, sending data back to the client
            PrintStream out = new PrintStream(socket.getOutputStream());

            out.println("What is red and smells like blue paint? Red paint.");

            // closing the connection with the client
            socket.close();
        } catch (IOException e) {
            // if any exceptions are thrown during the socket operations (getting the input and output streams,
            // closing the socket), they will be printed here
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

