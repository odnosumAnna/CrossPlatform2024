import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ConferenceManagerRemote extends Remote {
    void addParticipant(Participant participant) throws RemoteException;
    List<Participant> getParticipants() throws RemoteException;
    String getParticipantsAsXML() throws RemoteException;
}
