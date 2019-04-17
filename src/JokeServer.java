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
import java.net.*;

public class JokeServer {

    private static boolean jokeMode;
    private final static String JOKE_MODE = "joke mode";
    private final static String PROVERB_MODE = "proverb mode";

    public static void main(String[] args) throws IOException {
        File file = new File("LogFile.txt");
        Writer fileWriter = new FileWriter(file, false);

        int qLen = 6;
        // defining the port on which to start the server socket for client connections
        int clientPort = 4546;
        // defining the port on which to start the server socket for admin connections
        int adminPort = 5051;

        // setting to joke mode initially
        jokeMode = true;

        // Creating new thread to handle client connections. Passing in server socket for clients to connect to
        new ClientThread(new ServerSocket(clientPort, qLen), fileWriter).start();
        // Creating new thread to handle admin connections. Passing in server socket for admin to connect to
        new AdminThread(new ServerSocket(adminPort, qLen), fileWriter).start();

        // letting the user know that the server has started, and on which port
        String greeting = "Olivia Chisman's Inet server 1.8 starting up in " + getModeName()
                + " mode, listening to port " + clientPort + ".\n";
        System.out.println(greeting);
        fileWriter.write(greeting + "\n");
        fileWriter.flush();
    }

    public static String getModeName() {
        // returns the joke mode string based on the jokeMode variable being true or not
        return jokeMode ? JOKE_MODE : PROVERB_MODE;
    }

    public static boolean isInJokeMode() {
        return jokeMode;
    }

    public static void switchMode() {
        // changes the joke mode variable to be true if it is false, and false if it is true
        jokeMode = !jokeMode;
    }
}

class ClientThread extends Thread {
    private ServerSocket serverSocket;
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

    public AdminThread(ServerSocket serverSocket, Writer fileWriter) {
        this.serverSocket = serverSocket;
        this.fileWriter = fileWriter;
    }

    public void run() {
        // process runs in infinite loop, unless exception is thrown
        while (true) {
            try {
                // If any admins connect, the server mode variable with be switched to the opposite
                serverSocket.accept();
                JokeServer.switchMode();
                // Logging server mode
                String serverStatus = "Server now in " + JokeServer.getModeName();
                System.out.println(serverStatus);
                fileWriter.write(serverStatus + "\n");
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Worker extends Thread {

    private Socket socket;
    private Writer fileWriter;

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

            String completedCycle = fromClient.readLine();
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

