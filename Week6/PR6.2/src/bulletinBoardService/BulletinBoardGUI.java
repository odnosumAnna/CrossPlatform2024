package bulletinBoardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class BulletinBoardGUI {
    private static MulticastSenderReceiver[] senderReceivers = new MulticastSenderReceiver[4];

    private static class EDTInvocationHandler implements InvocationHandler {
        private Object invocationResult = null;
        private UITasks ui;

        public EDTInvocationHandler(UITasks ui) {
            this.ui = ui;
        }

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
            if (SwingUtilities.isEventDispatchThread()) {
                invocationResult = method.invoke(ui, args);
            } else {
                Runnable shell = () -> {
                    try {
                        invocationResult = method.invoke(ui, args);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                };
                SwingUtilities.invokeAndWait(shell);
            }
            return invocationResult;
        }
    }

    public interface UITasks {
        String getMessage();

        void appendMessage(String txt);

        void setGroupInfo(String group, int port, String name);
    }

    private static class UITasksImpl implements UITasks {
        private JTextField textFieldMsg;
        private JTextArea textArea;
        private JLabel groupLabel;

        public UITasksImpl(JTextField textFieldMsg, JTextArea textArea, JLabel groupLabel) {
            this.textFieldMsg = textFieldMsg;
            this.textArea = textArea;
            this.groupLabel = groupLabel;
        }

        @Override
        public String getMessage() {
            String res = textFieldMsg.getText();
            textFieldMsg.setText("");
            return res;
        }

        @Override
        public void appendMessage(String txt) {
            textArea.append(txt + "\n");
        }

        @Override
        public void setGroupInfo(String group, int port, String name) {
            groupLabel.setText("Group: " + group + " | Port: " + port + " | Name: " + name);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            createBulletinBoardGUI(i);
        }
    }

    private static void createBulletinBoardGUI(int index) {
        JFrame frame = new JFrame("Bulletin Board - User " + (index + 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));

        JTextField textFieldMsg = new JTextField();
        panel.add(textFieldMsg);

        JButton sendButton = new JButton("Send");
        panel.add(sendButton);

        JButton connectButton = new JButton("Connect");
        panel.add(connectButton);

        JButton disconnectButton = new JButton("Disconnect");
        panel.add(disconnectButton);

        JButton clearButton = new JButton("Clear");
        panel.add(clearButton);

        JButton exitButton = new JButton("Exit");
        panel.add(exitButton);

        JLabel groupLabel = new JLabel();
        panel.add(groupLabel);

        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);

        UITasks ui = (UITasks) Proxy.newProxyInstance(BulletinBoardGUI.class.getClassLoader(),
                new Class[]{UITasks.class},
                new EDTInvocationHandler(new UITasksImpl(textFieldMsg, textArea, groupLabel)));

        senderReceivers[index] = new MulticastSenderReceiver(ui);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = ui.getMessage();
                senderReceivers[index].sendMessage(message);
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                senderReceivers[index].connect();
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                senderReceivers[index].disconnect();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}