import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ClientHandler implements Runnable {
    private static Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println(socket);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your name:");
            name = in.readLine();
            out.println("Welcome, " + name + "!");

            tema4_4_server.broadcast(name + " joined the chat!", this);

            String line;
            while ((line = in.readLine()) != null) {
                if (line.equalsIgnoreCase("/quit")) {
                    break;
                }
                if (line.equalsIgnoreCase("/shutdown") && name.equals("admin")) {
                    tema4_4_server.shutdownServer();
                    break;
                }
                tema4_4_server.broadcast(name + ": " + line, this);
            }
        } catch (IOException e) {
            System.out.println("Error with client: " + e.getMessage());
        }
        tema4_4_server.removeClient(this);
        tema4_4_server.broadcast(name + " has left the chat.", this);
        System.out.println(name + " has left the chat.");
        close();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


public class tema4_4_server {
    private static final int PORT = 8888;
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private static volatile boolean isRunning = true;

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (isRunning) {
                var clientSocket = serverSocket.accept();
                var handler = new ClientHandler(clientSocket);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            if (isRunning)
                e.printStackTrace();
        } finally {
            shutdownServer();
        }
    }

    public static void broadcast(String message, ClientHandler exclude) {
        for (ClientHandler client : clients) {
            if (client != exclude) {
                client.sendMessage(message);
            }
        }
    }

    public static void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    public static void shutdownServer() {
        isRunning = false;
        System.out.println("Shutting down server...");
        for (ClientHandler client : clients) {
            client.sendMessage("Server is shutting down...");
            client.close();
        }
        clients.clear();
    }

}
