import java.util.List;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private JFrame frame;
    private JTextArea textArea;
    private ConferenceManager conferenceManager;
    private ConferenceManagerRemoteImpl conferenceManagerRemote;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Server window = new Server();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Server() {
        conferenceManager = new ConferenceManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        panel.add(btnStart);

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
        panel.add(btnStop);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        panel.add(btnSave);

        JButton btnLoad = new JButton("Load");
        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        panel.add(btnLoad);

        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(btnExit);

        textArea = new JTextArea();
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void startServer() {
        try {
            conferenceManagerRemote = new ConferenceManagerRemoteImpl(conferenceManager, this);
            Registry registry = LocateRegistry.createRegistry(1098); // Зміна порту на 1098
            registry.rebind("ConferenceManager", conferenceManagerRemote);
            appendMessage("Server started on port 1098.");
        } catch (Exception e) {
            appendMessage("Error starting server: " + e.getMessage());
        }
    }


    private void stopServer() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.unbind("ConferenceManager");
            UnicastRemoteObject.unexportObject(conferenceManagerRemote, true);
            appendMessage("Server stopped.");
        } catch (Exception e) {
            appendMessage("Error stopping server: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            String xmlData = conferenceManagerRemote.getParticipantsAsXML();
            String fileName = "participants.xml";
            XMLFileHandler.saveToXMLFile(fileName, xmlData);
            appendMessage("Data saved to " + fileName);
        } catch (Exception e) {
            appendMessage("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            String fileName = "participants.xml";
            List<Participant> participants = XMLFileHandler.loadFromXMLFile(fileName);
            for (Participant participant : participants) {
                conferenceManager.addParticipant(participant);
            }
            appendMessage("Data loaded from " + fileName);
        } catch (Exception e) {
            appendMessage("Error loading data: " + e.getMessage());
        }
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
            System.out.println(message);
        });
    }
}
