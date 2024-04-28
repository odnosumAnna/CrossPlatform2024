package tcpWork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MetroServer extends Thread {
    private MetroCardBank cardBank;
    private ServerSocket serverSocket;
    private int serverPort;

    public MetroServer(int port) {
        this.cardBank = new MetroCardBank();
        this.serverPort = port;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(serverPort);
            System.out.println("Metro Server started");
            while (true) {
                System.out.println("Waiting for new client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(cardBank, clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } finally {
            try {
                serverSocket.close();
                System.out.println("Metro Server stopped");
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
        }
    }

    public static void main(String[] args) {
        MetroServer server = new MetroServer(7891);
        server.start();

        MetroClient.main(null);
    }
}
