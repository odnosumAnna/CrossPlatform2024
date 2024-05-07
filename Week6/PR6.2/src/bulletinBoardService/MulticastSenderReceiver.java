package bulletinBoardService;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
class MulticastSenderReceiver {
    private String name;
    private InetAddress addr;
    private int port = 8888;
    private MulticastSocket group;
    private BulletinBoardGUI.UITasks ui;

    public MulticastSenderReceiver(BulletinBoardGUI.UITasks ui) {
        this.name = JOptionPane.showInputDialog("Enter your name:");
        this.ui = ui;
    }


    public void connect() {
        try {
            addr = InetAddress.getByName("224.0.0.1");
            group = new MulticastSocket(port);
            group.joinGroup(addr);
            new Receiver().start();
            ui.setGroupInfo(addr.getHostAddress(), port, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (group != null) {
            try {
                group.leaveGroup(addr);
                group.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            message = name + ": " + message;
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
            group.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Receiver extends Thread {
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    group.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    ui.appendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}