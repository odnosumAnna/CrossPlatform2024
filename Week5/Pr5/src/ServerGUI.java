import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerGUI {
    private ObjectOutputStream out;
    private static final Logger logger = Logger.getLogger(ServerGUI.class.getName());

    public static void main(String[] args) {
        new ServerGUI().runServer();
    }

    public void runServer() {
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JTextArea logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        frame.getContentPane().add(new JScrollPane(logTextArea));

        frame.setVisible(true);

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            logger.info("Server started. Waiting for clients...");

            while (true) {

                Socket clientSocket = serverSocket.accept();
                logger.info("Client connected: " + clientSocket);

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                String taskClassName = (String) in.readObject();
                Executable job = (Executable) in.readObject();

                Result r = (Result) job.execute();

                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(r);

                clientSocket.close();
                
                logTextArea.append("Task executed. Result: " + r + "\n");
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Exception occurred", ex);
        }
    }
}
