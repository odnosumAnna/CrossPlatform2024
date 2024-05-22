import java.io.StringWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConferenceManagerRemoteImpl extends UnicastRemoteObject implements ConferenceManagerRemote {
    private ConferenceManager conferenceManager;
    private Server server;

    public ConferenceManagerRemoteImpl(ConferenceManager conferenceManager, Server server) throws RemoteException {
        this.conferenceManager = conferenceManager;
        this.server = server;
    }

    @Override
    public void addParticipant(Participant participant) throws RemoteException {
        conferenceManager.addParticipant(participant);
        server.appendMessage("Participant added: " + participant.getName() + " " + participant.getFamilyName() +
                ", Place of Work: " + participant.getPlaceOfWork() + ", Report Title: " + participant.getReportTitle() +
                ", Email: " + participant.getEmail());
    }

    @Override
    public List<Participant> getParticipants() throws RemoteException {
        List<Participant> participants = conferenceManager.getParticipants();
        for (Participant participant : participants) {
            server.appendMessage("Participant retrieved: " + participant.getName() + " " + participant.getFamilyName() +
                    ", Place of Work: " + participant.getPlaceOfWork() + ", Report Title: " + participant.getReportTitle() +
                    ", Email: " + participant.getEmail());
        }
        return participants;
    }

    @Override
    public String getParticipantsAsXML() throws RemoteException {
        List<Participant> participants = conferenceManager.getParticipants();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("RegisteredConferees");
            doc.appendChild(rootElement);

            for (Participant p : participants) {
                Element conferee = doc.createElement("Conferee");

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(p.getName()));
                conferee.appendChild(name);

                Element familyName = doc.createElement("familyName");
                familyName.appendChild(doc.createTextNode(p.getFamilyName()));
                conferee.appendChild(familyName);

                Element placeOfWork = doc.createElement("placeOfWork");
                placeOfWork.appendChild(doc.createTextNode(p.getPlaceOfWork()));
                conferee.appendChild(placeOfWork);

                Element reportTitle = doc.createElement("reportTitle");
                reportTitle.appendChild(doc.createTextNode(p.getReportTitle()));
                conferee.appendChild(reportTitle);

                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(p.getEmail()));
                conferee.appendChild(email);

                rootElement.appendChild(conferee);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            server.appendMessage("Data retrieved as XML");

            return writer.toString();

        } catch (Exception e) {
            server.appendMessage("Error converting to XML: " + e.getMessage());
            throw new RemoteException("Error converting to XML", e);
        }
    }
}
