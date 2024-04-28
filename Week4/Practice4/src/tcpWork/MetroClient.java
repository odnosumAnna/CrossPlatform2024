package tcpWork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MetroClient {
    private int port = -1;
    private String server = null;
    private Socket socket = null;
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;

    public MetroClient(String server, int port) {
        this.port = port;
        this.server = server;
        try {
            socket = new Socket(server, port);
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void finish() {
        try {
            os.writeObject(new StopOperation());
            os.flush();
            System.out.println(is.readObject());
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void applyOperation(CardOperation op) {
        try {
            if (os != null) { // Перевіряємо, чи ініціалізовано os
                os.writeObject(op);
                os.flush();
                System.out.println(is.readObject());
            } else {
                System.out.println("Error: ObjectOutputStream is not initialized");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static void main(String[] args) {
        MetroClient client = new MetroClient("localhost", 7891);
        IssueCardOperation op = new IssueCardOperation("12345");
        client.applyOperation(op);
        client.finish();
    }
}
