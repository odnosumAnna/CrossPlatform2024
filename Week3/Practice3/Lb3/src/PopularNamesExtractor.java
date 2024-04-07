import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PopularNamesExtractor {

    public static void main(String[] args) throws Exception {
        String ethnicity = "HISPANIC"; // Задайте бажану етнічну групу
        int numberOfNames = 5; // Задайте бажану кількість найбільш популярних імен

        List<NameInfo> popularNames = getPopularNames(ethnicity, numberOfNames);

        // Сортування за рейтингом
        popularNames.sort(Comparator.comparingInt(NameInfo::getRating));

        // Виведення інформації про кожне ім'я в консоль
        System.out.println("Найпопулярніші імена в етнічній групі " + ethnicity + ":");
        for (NameInfo nameInfo : popularNames) {
            System.out.println("Ім'я: " + nameInfo.getName() + ", Гендер: " + nameInfo.getGender() +
                    ", Кількість: " + nameInfo.getCount() + ", Рейтинг: " + nameInfo.getRating());
        }

        // Зберігання відсортованих даних до нового XML файлу
        saveToXml(popularNames, "Popular_Names_Output.xml");
    }

    private static List<NameInfo> getPopularNames(String ethnicity, int numberOfNames) {
        List<NameInfo> popularNames = new ArrayList<>();

        try {
            // Завантаження XML файлу
            File inputFile = new File("Popular_Baby_Names_NY.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Отримуємо список всіх елементів <row>
            NodeList nodeList = doc.getElementsByTagName("row");

            // Проходимося по кожному елементу
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String ethcty = element.getElementsByTagName("ethcty").item(0).getTextContent();
                if (ethcty.equalsIgnoreCase(ethnicity)) {
                    // Отримуємо дані про ім'я, гендер, кількість та рейтинг
                    String name = element.getElementsByTagName("nm").item(0).getTextContent();
                    String gender = element.getElementsByTagName("gndr").item(0).getTextContent();
                    int count = Integer.parseInt(element.getElementsByTagName("cnt").item(0).getTextContent());
                    int rating = Integer.parseInt(element.getElementsByTagName("rnk").item(0).getTextContent());

                    // Додаємо інформацію про ім'я до списку
                    popularNames.add(new NameInfo(name, gender, count, rating));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Обмеження кількості найпопулярніших імен, якщо це необхідно
        if (popularNames.size() > numberOfNames) {
            return popularNames.subList(0, numberOfNames);
        } else {
            return popularNames;
        }
    }

    private static void saveToXml(List<NameInfo> names, String filename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Створюємо новий документ XML
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("PopularNames");
            doc.appendChild(rootElement);

            // Додаємо інформацію про кожне ім'я до XML
            for (NameInfo nameInfo : names) {
                Element nameElement = doc.createElement("Name");
                rootElement.appendChild(nameElement);

                Element name = doc.createElement("Name");
                name.appendChild(doc.createTextNode(nameInfo.getName()));
                nameElement.appendChild(name);

                Element gender = doc.createElement("Gender");
                gender.appendChild(doc.createTextNode(nameInfo.getGender()));
                nameElement.appendChild(gender);

                Element count = doc.createElement("Count");
                count.appendChild(doc.createTextNode(String.valueOf(nameInfo.getCount())));
                nameElement.appendChild(count);

                Element rating = doc.createElement("Rating");
                rating.appendChild(doc.createTextNode(String.valueOf(nameInfo.getRating())));
                nameElement.appendChild(rating);
            }

            // Зберігаємо документ XML у файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

            System.out.println("Дані успішно збережено в файл " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class NameInfo {
    private String name;
    private String gender;
    private int count;
    private int rating;

    public NameInfo(String name, String gender, int count, int rating) {
        this.name = name;
        this.gender = gender;
        this.count = count;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getCount() {
        return count;
    }

    public int getRating() {
        return rating;
    }
}
