import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ConferenceManager {
    private List<Participant> participants = new ArrayList<>();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addParticipant(Participant participant) {
        participants.add(participant);
        support.firePropertyChange("participants", null, participant);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        support.firePropertyChange("participants", participant, null);
    }

    public List<Participant> getParticipants() {
        return new ArrayList<>(participants);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
