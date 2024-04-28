package tcpWork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MetroCardBank cardBank;
    private Socket clientSocket;

    public ClientHandler(MetroCardBank cardBank, Socket clientSocket) {
        this.cardBank = cardBank;
        this.clientSocket = clientSocket;
        try {
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = inputStream.readObject();
                if (request instanceof CardOperation) {
                    processOperation((CardOperation) request);
                } else {
                    System.out.println("Received unknown object");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e);
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    private void processOperation(CardOperation operation) throws IOException {
        if (operation instanceof IssueCardOperation) {
            IssueCardOperation op = (IssueCardOperation) operation;
            boolean issued = cardBank.issueCard(op.getStudentID());
            if (issued) {
                outputStream.writeObject("Card issued and registered");
            } else {
                outputStream.writeObject("Failed to issue card");
            }
        } else if (operation instanceof GetClientInfoOperation) {
            GetClientInfoOperation op = (GetClientInfoOperation) operation;
            String info = cardBank.getClientInfo(op.getCardID());
            outputStream.writeObject(info);
        } else if (operation instanceof RechargeOperation) {
            RechargeOperation op = (RechargeOperation) operation;
            boolean recharged = cardBank.rechargeCard(op.getCardID(), op.getAmount());
            if (recharged) {
                outputStream.writeObject("Card balance recharged");
            } else {
                outputStream.writeObject("Failed to recharge card balance");
            }
        } else if (operation instanceof PayOperation) {
            PayOperation op = (PayOperation) operation;
            boolean paid = cardBank.payFare(op.getCardID(), op.getFare());
            if (paid) {
                outputStream.writeObject("Fare paid");
            } else {
                outputStream.writeObject("Failed to pay fare");
            }
        } else if (operation instanceof GetBalanceOperation) {
            GetBalanceOperation op = (GetBalanceOperation) operation;
            double balance = cardBank.getCardBalance(op.getCardID());
            outputStream.writeObject("Card balance: " + balance);
        } else {
            outputStream.writeObject("Invalid operation");
        }
        outputStream.flush();
    }
}

