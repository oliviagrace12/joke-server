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

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliviachisman on 4/7/19
 */
public class JokeClient {

    private static final int PORT = 4545;

    // creating lists to be filled with jokes and proverbs
    private static List<String> jokes = new ArrayList<>();
    private static List<String> proverbs = new ArrayList<>();
    private static String name;

    public static void main(String[] args) throws IOException {
        // populating list of jokes
        jokes.add("JA You know why you never see elephants hiding up in trees? Because they’re really good at it.");
        jokes.add("JB What is red and smells like blue paint? Red paint.");
        jokes.add("JC Why aren’t koalas actual bears? The don’t meet the koalafications.");
        jokes.add("JD What do you call bears with no ears? B.");
        // populating list of proverbs
        proverbs.add("PA Actions speak louder than words.");
        proverbs.add("PB A journey of a thousand miles begins with a single step.");
        proverbs.add("PC A watched pot never boils.");
        proverbs.add("PD Birds of a feather flock together.");

        String serverName;

        // reading in server name from program arguments, or setting to default if none specified
        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        // printing startup information to console
        System.out.println("Olivia Chisman's Inet client, 1.8.\n");
        System.out.println("Using server: " + serverName + ", PORT: " + PORT);

        // creating reader to read user input from keyboard
        BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
        // requesting user input by printing to console
        System.out.print("Please enter your name: ");
        // Reading user input from keyboard and storing in global variable for later use.
        // This will always be this client's name
        name = fromConsole.readLine();

        getJoke(serverName, fromConsole);
    }

    private static void getJoke(String serverName, BufferedReader fromConsole) {
        // indexes to keep track of which joke or proverb to send
        int jokeIndex = 0;
        int proverbIndex = 0;

        try {
            // variables to hold strings read from server and console
            String textFromServer;
            String textFromConsole;

            do {
                // creating a socket with the specified PORT and server name with which to connect to the server.
                // The PORT should be the same as that of the server in order to connect to the server
                Socket socket = new Socket(serverName, PORT);
                // creating a reader to read the data coming into the socket (coming from the server)
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // creating a stream to write to the socket, sending data to the server
                PrintStream toServer = new PrintStream(socket.getOutputStream());

                // reading the server mode from the socket, either joke or proverb
                textFromServer = fromServer.readLine();
                // different logic depending on joke or proverb mode
                if (textFromServer.equalsIgnoreCase("joke")) {
                    // If in joke mode, get a joke from the joke list based on which joke index we are currently on.
                    // Jokes are read in order as of now.
                    // Add the client name to the joke string
                    String joke = getFormattedMessage(name, jokes.get(jokeIndex));
                    // Print joke to client console
                    System.out.println(joke);
                    System.out.flush();
                    // print joke to server to be saved in the log
                    toServer.println(joke);
                    toServer.flush();
                    // set next joke index
                    jokeIndex = getNewIndex(jokeIndex, toServer, "JOKE");
                } else {
                    // If in proverb mode, get a proverb from the proverb list based on which proverb index we are currently on.
                    // Proverbs are read in order as of now.
                    // Add the client name to the proverb string
                    String proverb = getFormattedMessage(name, proverbs.get(proverbIndex));
                    // print proverb to client console
                    System.out.println(proverb);
                    System.out.flush();
                    // print proverb to socket to be read by server and saved in log
                    toServer.println(proverb);
                    toServer.flush();
                    // increment proverb index
                    proverbIndex = getNewIndex(proverbIndex, toServer, "PROVERB");
                }
                // closing the connection to the server
                socket.close();
                // wait for the user to press enter again
                textFromConsole = fromConsole.readLine();
                // if the user simply pressed enter (or typed anything other than quit), keep going.
                // if the user entered quit, end the program
            } while (textFromConsole == null || !textFromConsole.equalsIgnoreCase("quit"));


        } catch (IOException e) {
            // catches any exceptions thrown during reading from or writing to the socket
            System.out.println("Socket error");
            e.printStackTrace();
        }
    }

    private static int getNewIndex(int currentIndex, PrintStream toServer, String jokeOrProverb) {
        // If current index is 0-2, increment the index. Otherwise, reset to zero.
        // There are only 4 jokes and 4 proverbs.
        if (currentIndex < 3) {
            return currentIndex + 1;
        } else {
            // if cycle is restarting (index is being reset to zero), then print cycle complete message
            // to console and server socket
            String message = name + " " + jokeOrProverb + " CYCLE COMPLETED";
            System.out.println(message);
            System.out.flush();
            toServer.println(message);
            toServer.flush();
            return 0;
        }
    }

    private static String getFormattedMessage(String name, String jokeOrProverb) {
        // split joke or proverb string on first space in order to insert the user name into the proper position
        String[] split = jokeOrProverb.split(" ", 2);
        return split[0] + " " + name + " " + split[1];
    }
}
