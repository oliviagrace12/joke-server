import java.io.IOException;
import java.net.Socket;

/**
 * Created by oliviachisman on 4/13/19
 */
public class JokeClientAdmin {

    private static final int PORT = 5050;

    public static void main(String[] args) {
        String serverName;

        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        System.out.println("Olivia Chisman's Inet client admin, 1.8.\n");
        System.out.println("Using server: " + serverName + ", PORT: " + PORT);

        try {
            // creating a socket with the specified PORT and server name with which to connect to the server.
            // The PORT should be the same as that of the server in order to connect to the server
            // Each time the admin connects to the server, the server will switch modes.
            Socket socket = new Socket(serverName, PORT);
            // closing the connection to the server
            socket.close();
        } catch (IOException e) {
            // catches any exceptions thrown during reading from or writing to the socket
            System.out.println("Socket error");
            e.printStackTrace();
        }
    }
}
