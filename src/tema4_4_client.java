import java.io.*;
import java.net.*;

public class tema4_4_client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8888)) {
            var serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var serverOut = new PrintWriter(socket.getOutputStream(), true);
            var userIn = new BufferedReader(new InputStreamReader(System.in));

            Thread readThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = serverIn.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            Thread writeThread = new Thread(() -> {
                try {
                    String input;
                    while ((input = userIn.readLine()) != null) {
                        serverOut.println(input);
                        if (input.equalsIgnoreCase("/quit")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readThread.start();
            writeThread.start();

            readThread.join();
            writeThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
