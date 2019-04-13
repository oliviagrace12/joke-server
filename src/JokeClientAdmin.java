/**
 * Created by oliviachisman on 4/13/19
 */
public class JokeClientAdmin {

    private static int port = 4545;

    public static void main(String[] args) {
        String serverName;

        if (args.length >= 1) {
            serverName = args[0];
        } else {
            serverName = "localhost";
        }

        System.out.println("Olivia Chisman's Inet client admin, 1.8.\n");
        System.out.println("Using server: " + serverName + ", port: " + port);
    }
}
