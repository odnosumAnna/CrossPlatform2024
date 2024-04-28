package tcpWork;
import java.util.HashMap;
import java.util.Map;

public class MetroCardBank {
    private Map<String, Double> cardBalances;

    public MetroCardBank() {
        this.cardBalances = new HashMap<>();
    }

    public synchronized boolean issueCard(String studentID) {
        // Assuming card issuance is successful
        return true;
    }

    public synchronized String getClientInfo(String cardID) {
        // Assuming we retrieve client information from a database or some storage
        return "Client information for card ID: " + cardID;
    }

    public synchronized boolean rechargeCard(String cardID, double amount) {
        if (cardBalances.containsKey(cardID)) {
            cardBalances.put(cardID, cardBalances.get(cardID) + amount);
            return true;
        }
        return false;
    }

    public synchronized boolean payFare(String cardID, double fare) {
        if (cardBalances.containsKey(cardID) && cardBalances.get(cardID) >= fare) {
            cardBalances.put(cardID, cardBalances.get(cardID) - fare);
            return true;
        }
        return false;
    }

    public synchronized double getCardBalance(String cardID) {
        return cardBalances.getOrDefault(cardID, 0.0);
    }

    public synchronized boolean registerCard(String cardID) {
        if (!cardBalances.containsKey(cardID)) {
            cardBalances.put(cardID, 0.0);
            return true;
        }
        return false;
    }

    public synchronized boolean removeCard(String cardID) {
        if (cardBalances.containsKey(cardID)) {
            cardBalances.remove(cardID);
            return true;
        }
        return false;
    }
}
