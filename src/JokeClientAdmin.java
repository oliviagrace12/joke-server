import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by oliviachisman on 4/13/19
 */
public class JokeClientAdmin {

    private static int port = 5050;

    public static void main(String[] args) {
        String serverName;

        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        System.out.println("Olivia Chisman's Inet client admin, 1.8.\n");
        System.out.println("Using server: " + serverName + ", port: " + port);

        try {
            // creating a socket with the specified port and server name with which to connect to the server.
            // The port should be the same as that of the server in order to connect to the server
            Socket socket = new Socket(serverName, port);

            // creating a printstream to write to the server
//            PrintStream toServer = new PrintStream(socket.getOutputStream());
            // creating a reader to read input from the server
//            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // creating a reader to read user input from the keyboard
//            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

//            System.out.println("Please enter either joke or proverb to switch to desired server mode, or q to quit");
//            String mode;
//            do {
//                mode = userInput.readLine();
//                toServer.println(mode);
//
//                String error = fromServer.readLine();
//                if (error != null) {
//                    System.out.println(error);
//                }
//            } while (mode.equals("q"));
            // closing the connection to the server
            socket.close();
        } catch (IOException e) {
            // catches any exceptions thrown during reading from or writing to the socket
            System.out.println("Socket error");
            e.printStackTrace();
        }
    }
}
