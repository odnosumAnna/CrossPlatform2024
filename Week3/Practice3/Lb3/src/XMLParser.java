import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashSet;
import java.util.Set;

public class XMLParser {

    public static void main(String[] args) throws Exception {
        // Виклик парсера
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        // Парсінг XML документу
        saxParser.parse("Popular_Baby_Names_NY.xml", new MyHandler());
    }

    // Створення обробника
    static class MyHandler extends DefaultHandler {

        boolean bCharacters = false;
        Set<String> tagNames = new HashSet<>(); // Створюємо множину для зберігання унікальних імен тегів

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.println("Start Element: " + qName);
            tagNames.add(qName); // Додаємо ім'я тегу до множини
            bCharacters = true;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println("End Element: " + qName);
            bCharacters = false;
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            if (bCharacters) {
                System.out.println("Text: " + new String(ch, start, length));
            }
        }

        @Override
        public void endDocument() throws SAXException {
            // Виводимо усі унікальні імена тегів після завершення парсінгу
            System.out.println("List of tag names:");
            for (String tagName : tagNames) {
                System.out.println(tagName);
            }
        }
    }
}
