package parseBook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by strapper on 02.09.15.
 */
public class FB2Book extends Book {

    public FB2Book(byte[]  input, EnglishLexicon lexicon) {
        super(input, lexicon);
    }

    @Override
    public void parse(byte[]  xmlString) {
        {
            ArrayList<String> list = new ArrayList<String>();
            String text= "";
            try {
//            File fXmlFile = new File(bookTitle);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(xmlString);
              //  ByteArrayInputStream byteStream = new ByteArrayInputStream( xmlString.getBytes() );
                InputSource is = new InputSource(byteStream);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();

//            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getElementsByTagName("p");

                // get text from all paragraphs
                for(int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    Element eElement = (Element) nNode;
                    String item = eElement.getTextContent();
                    list.add(item);
                }

                for(String item: list) {
                    text += item;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            setText(text);
        }
    }
}
