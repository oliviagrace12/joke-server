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

import java.io.*;
import java.net.*;

public class JokeServer {

    // defining the port on which to start the server socket for client connections
    private static final int CLIENT_PORT = 4545;
    // defining the port on which to start the server socket for admin connections
    private static final int ADMIN_PORT = 5050;

    private static boolean jokeMode;

    public static void main(String[] args) throws IOException {
        // creating file to log the server's interactions with clients and admins
        File file = new File("LogFile.txt");
        // creating a file writer to write to the file. It will overwrite the existing file every time the program starts
        Writer fileWriter = new FileWriter(file, false);
        // setting to joke mode initially
        jokeMode = true;

        int qLen = 6;
        // Creating new thread to handle client connections. Passing in server socket for clients to connect to
        new ClientThread(new ServerSocket(CLIENT_PORT, qLen), fileWriter).start();
        // Creating new thread to handle admin connections. Passing in server socket for admin to connect to
        new AdminThread(new ServerSocket(ADMIN_PORT, qLen), fileWriter).start();

        // letting the user know that the server has started, and on which port
        String greeting = "Olivia Chisman's Inet server 1.8 starting up in " + getModeName()
                + " mode, listening to client port " + CLIENT_PORT + " and admin port " + ADMIN_PORT + ".\n";
        // writing to console
        System.out.println(greeting);
        // writing to log file
        fileWriter.write(greeting + "\n");
        // flushing to make sure writing gets done right away rather than when the file writer is closed
        fileWriter.flush();
    }

    public static String getModeName() {
        // returns the joke mode string based on the jokeMode variable being true or not
        return jokeMode ? "JOKE MODE" : "PROVERB MODE";
    }

    // returns true if server is in joke mode
    public static boolean isInJokeMode() {
        return jokeMode;
    }

    public static void switchMode() {
        // changes the joke mode variable to be true if it is false, and false if it is true
        jokeMode = !jokeMode;
    }
}

class ClientThread extends Thread {
    // server socket to accept connections from clients
    private ServerSocket serverSocket;
    // file writer to write to log file
    private final Writer fileWriter;

    public ClientThread(ServerSocket serverSocket, Writer fileWriter) {
        this.serverSocket = serverSocket;
        this.fileWriter = fileWriter;
    }

    public void run() {
        // process runs in infinite loop, unless exception is thrown
        while (true) {
            // if any clients try to connect, the serverSocket will accept their connections and return a socket
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // this socket is passed to a new thread that is spawned (an instance of the Worker class) and start
            // is called on this thread, kicking off the processes in the Worker class run method.
            if (clientSocket != null) {
                new Worker(clientSocket, fileWriter).start();
            }
        }
    }
}

class AdminThread extends Thread {
    private ServerSocket serverSocket;
    private Writer fileWriter;

    // takes a server socket to accept admin connections, as well as a file writer to log
    // admin interactions to the log file
    public AdminThread(ServerSocket serverSocket, Writer fileWriter) {
        this.serverSocket = serverSocket;
        this.fileWriter = fileWriter;
    }

    public void run() {
        // process runs in infinite loop, unless exception is thrown
        while (true) {
            try {
                // If any admins connect, the server mode variable with be switched to the opposite
                Socket socket = serverSocket.accept();
                JokeServer.switchMode();
                // Logging server mode
                String serverStatus = "Server now in " + JokeServer.getModeName();
                System.out.println(serverStatus);
                // Writing mode change to log file
                fileWriter.write(serverStatus + "\n");
                fileWriter.flush();
                // creating a stream to write to the socket, sending data back to the admin
                PrintStream toAdmin = new PrintStream(socket.getOutputStream());
                // sending server mode status back to admin
                toAdmin.println(serverStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Worker extends Thread {

    private Socket socket;
    private Writer fileWriter;

    // Worker takes a server socket to pass to the client, and a file writer to log the client interactions
    public Worker(Socket socket, Writer fileWriter) {
        this.socket = socket;
        this.fileWriter = fileWriter;
    }

    public void run() {
        try {
            // creating a stream to write to the socket, sending data back to the client
            PrintStream toClient = new PrintStream(socket.getOutputStream());
            // creating a reader to read from the socket, retrieving data from the client
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // creating a stream to write to the console
            PrintStream toConsole = new PrintStream(System.out);

            // checking server mode
            if (JokeServer.isInJokeMode()) {
                // printing a joke to the socket to be read by the client
                toClient.println("JOKE");
            } else {
                // printing a proverb to the socket to be read by the client
                toClient.println("PROVERB");
            }
            toClient.flush();

            // reading the joke or proverb back from the client, to be added to the JokeLog
            String jokeOrProverb = fromClient.readLine();
            toConsole.println(jokeOrProverb);
            fileWriter.write(jokeOrProverb + "\n");
            fileWriter.flush();

            // reading one more time from client in case it is sending the end-of-cycle message
            String completedCycle = fromClient.readLine();
            // if it gets an end-of-cycle message, print it to console and log file
            if (completedCycle != null) {
                toConsole.println(completedCycle);
                fileWriter.write(completedCycle + "\n");
                fileWriter.flush();
            }

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

