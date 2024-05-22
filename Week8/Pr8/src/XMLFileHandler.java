import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLFileHandler {

    public static void saveToXMLFile(String fileName, String xmlContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xmlContent);
            System.out.println("Data has been saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving data to XML file: " + e.getMessage());
        }
    }

    public static List<Participant> loadFromXMLFile(String fileName) {
        List<Participant> participants = new ArrayList<>();
        try {
            File file = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Conferee");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    Participant participant = new Participant();
                    participant.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    participant.setFamilyName(element.getElementsByTagName("familyName").item(0).getTextContent());
                    participant.setPlaceOfWork(element.getElementsByTagName("placeOfWork").item(0).getTextContent());
                    participant.setReportTitle(element.getElementsByTagName("reportTitle").item(0).getTextContent());
                    participant.setEmail(element.getElementsByTagName("email").item(0).getTextContent());

                    participants.add(participant);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from XML file: " + e.getMessage());
        }
        return participants;
    }
}
