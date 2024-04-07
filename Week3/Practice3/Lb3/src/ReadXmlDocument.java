import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ReadXmlDocument {

    public static void main(String[] args) {
        try {
            File inputFile = new File("Popular_Names_Output.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nameList = doc.getElementsByTagName("Name");

            System.out.println("Дані з XML документу:");

            for (int i = 0; i < nameList.getLength(); i++) {
                Element nameElement = (Element) nameList.item(i);
                String name = "";
                String gender = "";
                String count = "";
                String rating = "";

                // Перевіряємо, чи існує дочірній елемент <Name>
                if (nameElement.getElementsByTagName("Name").getLength() > 0) {
                    name = nameElement.getElementsByTagName("Name").item(0).getTextContent();
                }

                // Перевіряємо, чи існує дочірній елемент <Gender>
                if (nameElement.getElementsByTagName("Gender").getLength() > 0) {
                    gender = nameElement.getElementsByTagName("Gender").item(0).getTextContent();
                }

                // Перевіряємо, чи існує дочірній елемент <Count>
                if (nameElement.getElementsByTagName("Count").getLength() > 0) {
                    count = nameElement.getElementsByTagName("Count").item(0).getTextContent();
                }

                // Перевіряємо, чи існує дочірній елемент <Rating>
                if (nameElement.getElementsByTagName("Rating").getLength() > 0) {
                    rating = nameElement.getElementsByTagName("Rating").item(0).getTextContent();
                }

                System.out.println("Ім'я: " + name + ", Гендер: " + gender +
                        ", Кількість: " + count + ", Рейтинг: " + rating);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
