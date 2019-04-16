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
import java.util.Map;

/**
 * Created by oliviachisman on 4/7/19
 */
public class JokeClient {

    private static int port = 4546;
    private static List<String> jokes = new ArrayList<>();
    private static List<String> proverbs = new ArrayList<>();
    private static String name;

    public static void main(String[] args) throws IOException {
        jokes.add("JA You know why you never see elephants hiding up in trees? Because they’re really good at it.");
        jokes.add("JB What is red and smells like blue paint? Red paint.");
        jokes.add("JC Why aren’t koalas actual bears? The don’t meet the koalafications.");
        jokes.add("JD What do you call bears with no ears? B.");

        proverbs.add("PA Actions speak louder than words.");
        proverbs.add("PB A journey of a thousand miles begins with a single step.");
        proverbs.add("PC A watched pot never boils.");
        proverbs.add("PD Birds of a feather flock together.");

        String serverName;

        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        System.out.println("Olivia Chisman's Inet client, 1.8.\n");
        System.out.println("Using server: " + serverName + ", port: " + port);

        BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter your name: ");
        name = fromConsole.readLine();

        getJoke(serverName, fromConsole);
    }

    private static void getJoke(String serverName, BufferedReader fromConsole) {
        int jokeIndex = 0;
        int proverbIndex = 0;

        try {
            // reading from server
            String textFromServer;
            String textFromConsole;

            do {
                // creating a socket with the specified port and server name with which to connect to the server.
                // The port should be the same as that of the server in order to connect to the server
                Socket socket = new Socket(serverName, port);
                // creating a reader to read the data coming into the socket (coming from the server)
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream());

                textFromServer = fromServer.readLine();
                if (textFromServer.equalsIgnoreCase("joke")) {
                    String joke = jokes.get(jokeIndex);
                    System.out.println(joke);
                    out.println(getMessageForServer(name, joke));
                    jokeIndex = getNewIndex(jokeIndex);
                } else {
                    String proverb = proverbs.get(proverbIndex);
                    System.out.println(proverb);
                    out.println(getMessageForServer(name, proverb));
                    proverbIndex = getNewIndex(proverbIndex);
                }
                // closing the connection to the server
                socket.close();
                textFromConsole = fromConsole.readLine();
            } while (textFromConsole == null || !textFromConsole.equalsIgnoreCase("quit"));


        } catch (IOException e) {
            // catches any exceptions thrown during reading from or writing to the socket
            System.out.println("Socket error");
            e.printStackTrace();
        }
    }

    private static int getNewIndex(int currentIndex) {
        return currentIndex < 3 ? currentIndex + 1 : 0;
    }

    private static String getMessageForServer(String name, String jokeOrProverb) {
        String[] split = jokeOrProverb.split(" ", 2);
        return split[0] + " " + name + " " + split[1];
    }
}
