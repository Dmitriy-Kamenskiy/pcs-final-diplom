import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        try (
                Socket clientSocket = new Socket("localhost", 8989);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        ) {
            out.println("бизнес");
            System.out.println(in.readLine());

        }
    }
}