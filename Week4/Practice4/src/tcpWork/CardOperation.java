package tcpWork;

import java.io.Serializable;

public abstract class CardOperation implements Serializable {

}


class IssueCardOperation extends CardOperation {
    private String studentID;

    public IssueCardOperation(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentID() {
        return studentID;
    }
}


class GetClientInfoOperation extends CardOperation {
    private String cardID;

    public GetClientInfoOperation(String cardID) {
        this.cardID = cardID;
    }

    public String getCardID() {
        return cardID;
    }
}


class RechargeOperation extends CardOperation {
    private String cardID;
    private double amount;

    public RechargeOperation(String cardID, double amount) {
        this.cardID = cardID;
        this.amount = amount;
    }

    public String getCardID() {
        return cardID;
    }

    public double getAmount() {
        return amount;
    }
}


class PayOperation extends CardOperation {
    private String cardID;
    private double fare;

    public PayOperation(String cardID, double fare) {
        this.cardID = cardID;
        this.fare = fare;
    }

    public String getCardID() {
        return cardID;
    }

    public double getFare() {
        return fare;
    }
}


class GetBalanceOperation extends CardOperation {
    private String cardID;

    public GetBalanceOperation(String cardID) {
        this.cardID = cardID;
    }

    public String getCardID() {
        return cardID;
    }
}
