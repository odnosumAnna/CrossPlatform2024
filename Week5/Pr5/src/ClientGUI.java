import java.math.BigInteger;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientGUI {
    private ObjectOutputStream out;

    public static void main(String[] args) {
        new ClientGUI().runClient();
    }

    public void runClient() {
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField inputField = new JTextField(10);
        JButton sendButton = new JButton("Send Task");
        JLabel resultLabel = new JLabel();

        sendButton.addActionListener(e -> {
            try {
                String host = "localhost";
                int port = 12345;
                Socket clientSocket = new Socket(host, port);
                out = new ObjectOutputStream(clientSocket.getOutputStream());

                String taskClassName = "JobOne";
                out.writeObject(taskClassName);

                int num = Integer.parseInt(inputField.getText());
                Executable job = new JobOne(num);
                out.writeObject(job);

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                Result r = (Result) in.readObject();
                BigInteger result = (BigInteger) r.output();
                double executionTime = r.scoreTime();
                resultLabel.setText("Result: " + result + ", Execution Time: " + executionTime + " ms");

                System.out.println("Result: " + result + ", Execution Time: " + executionTime + " ms");

                clientSocket.close();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();

                System.err.println("Exception occurred: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();

                System.err.println("NumberFormatException occurred: " + ex.getMessage());
            }
        });
        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);
        panel.add(resultLabel);
        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
