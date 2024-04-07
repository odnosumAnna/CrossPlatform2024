import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;
import java.util.Set;

public class NationalGroupsExtractor {

    public static void main(String[] args) throws Exception {
        Set<String> nationalGroups = new HashSet<>();

        // Виклик парсера
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        // Парсінг XML документу
        saxParser.parse("Popular_Baby_Names_NY.xml", new MyHandler(nationalGroups));

        // Виведення назв національних груп у консоль
        System.out.println("Назви національних груп, представлених у документі:");
        for (String group : nationalGroups) {
            System.out.println(group);
        }
    }

    static class MyHandler extends DefaultHandler {
        private Set<String> nationalGroups;
        private boolean isInEthcty;

        public MyHandler(Set<String> nationalGroups) {
            this.nationalGroups = nationalGroups;
            this.isInEthcty = false;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("ethcty")) {
                isInEthcty = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isInEthcty) {
                String ethcty = new String(ch, start, length).trim();
                if (!ethcty.isEmpty()) {
                    nationalGroups.add(ethcty);
                }
                isInEthcty = false;
            }
        }
    }
}
