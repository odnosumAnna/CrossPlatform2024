import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.util.List;

public class Client {
    private JFrame frame;
    private JTextField txtName;
    private JTextField txtFamilyName;
    private JTextField txtPlaceOfWork;
    private JTextField txtReportTitle;
    private JTextField txtEmail;
    private JTextArea textArea;
    private ConferenceManagerRemote conferenceManager;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client window = new Client();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client() {
        initialize();
        connectToServer();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(7, 2));

        panel.add(new JLabel("Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Family Name:"));
        txtFamilyName = new JTextField();
        panel.add(txtFamilyName);

        panel.add(new JLabel("Place Of Work:"));
        txtPlaceOfWork = new JTextField();
        panel.add(txtPlaceOfWork);

        panel.add(new JLabel("Report Title:"));
        txtReportTitle = new JTextField();
        panel.add(txtReportTitle);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerParticipant();
            }
        });
        panel.add(btnRegister);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        panel.add(btnClear);

        JButton btnGetInfo = new JButton("Get Info");
        btnGetInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getInfo();
            }
        });
        panel.add(btnGetInfo);

        JButton btnFinish = new JButton("Finish");
        btnFinish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finishRegistration();
            }
        });
        panel.add(btnFinish);

        textArea = new JTextArea();
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1098); // Зміна порту на 1098
            conferenceManager = (ConferenceManagerRemote) registry.lookup("ConferenceManager");
            appendMessage("Connected to server on port 1098.");
        } catch (Exception e) {
            appendMessage("Error connecting to server: " + e.getMessage());
        }
    }


    private void registerParticipant() {
        try {
            Participant participant = new Participant();
            participant.setName(txtName.getText());
            participant.setFamilyName(txtFamilyName.getText());
            participant.setPlaceOfWork(txtPlaceOfWork.getText());
            participant.setReportTitle(txtReportTitle.getText());
            participant.setEmail(txtEmail.getText());

            conferenceManager.addParticipant(participant);
            String registrationMessage = "Participant registered: " + participant.getName() + " " + participant.getFamilyName();
            appendMessage(registrationMessage);

        } catch (Exception e) {
            appendMessage("Error registering participant: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtFamilyName.setText("");
        txtPlaceOfWork.setText("");
        txtReportTitle.setText("");
        txtEmail.setText("");
        appendMessage("Fields cleared.");
    }

    private void getInfo() {
        try {
            List<Participant> participants = conferenceManager.getParticipants();
            StringBuilder info = new StringBuilder("Participants Information:\n");
            for (Participant participant : participants) {
                info.append("Name: ").append(participant.getName()).append("\n");
                info.append("Family Name: ").append(participant.getFamilyName()).append("\n");
                info.append("Place of Work: ").append(participant.getPlaceOfWork()).append("\n");
                info.append("Report Title: ").append(participant.getReportTitle()).append("\n");
                info.append("Email: ").append(participant.getEmail()).append("\n\n");
            }
            appendMessage(info.toString());
        } catch (Exception e) {
            appendMessage("Error getting information: " + e.getMessage());
        }
    }

    private void finishRegistration() {
        frame.dispose();
    }

    private void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
            System.out.println(message);
        });
    }
}
