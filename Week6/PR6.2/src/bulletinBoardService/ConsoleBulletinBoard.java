package bulletinBoardService;

import java.util.Scanner;

public class ConsoleBulletinBoard {
    private static MulticastSenderReceiver senderReceiver;

    public static void main(String[] args) {
        senderReceiver = new MulticastSenderReceiver(new BulletinBoardGUI.UITasks() {
            @Override
            public String getMessage() {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter your message: ");
                return scanner.nextLine();
            }

            @Override
            public void appendMessage(String txt) {
                System.out.println(txt);
            }

            @Override
            public void setGroupInfo(String group, int port, String name) {
                System.out.println("Group: " + group + " | Port: " + port + " | Name: " + name);
            }
        });

        senderReceiver.connect();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Send message");
            System.out.println("2. Disconnect");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    sendMessage();
                    break;
                case 2:
                    senderReceiver.disconnect();
                    break;
                case 3:
                    senderReceiver.disconnect();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void sendMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your message: ");
        String message = scanner.nextLine();
        senderReceiver.sendMessage(message);
    }
}

