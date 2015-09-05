package parseBook;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.*;

/**
 * Created by strapper on 24.08.15.
 */
public class fb2Parser {


    public static String getParagraphs(String xmlString) {
        ArrayList<String> list = new ArrayList<String>();
        String text= "";
        try {
//            File fXmlFile = new File(bookTitle);
            InputSource is = new InputSource(new StringReader(xmlString));

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
        return text;
    }

    // get words from all paragraphs
    public static ArrayList<String> getWords(String sentences) {
        ArrayList<String> words = new ArrayList<String>();

            String[] array = sentences.split(" ");
            for(String element: array) {
                String word = element.trim();

                word = word.trim();
                word = StringUtils.strip(word, ",");
                word = StringUtils.strip(word, ".");

                if(!word.equals("")) {
                    words.add(word.toLowerCase());
                }
            }

        return words;
    }

    public static ArrayList<String> deleteEasyWords(ArrayList<String> words, ArrayList<String> lexicon) {

        Iterator<String> iterator = words.iterator();
        while(iterator.hasNext()) {
            String item = iterator.next();
            if(lexicon.contains(item)) {
                iterator.remove();
            }
        }
        return words;
    }

    // get the most popular words
    public static ArrayList<Word> getPopularWords(ArrayList<String> words) {
        TreeMap<String, Integer> wordsFrequency = new TreeMap<String, Integer>();
        for(String item: words) {
            if(wordsFrequency.containsKey(item)) {
                int frequency = wordsFrequency.get(item);
                wordsFrequency.put(item, frequency + 1);
            } else {
                wordsFrequency.put(item, 1);
            }
        }

        ArrayList<Word> popularWords = new ArrayList<Word>();
        for(Map.Entry<String,Integer> entry: wordsFrequency.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            Word word = new Word(key, value);
            popularWords.add(word);
        }

        Collections.sort(popularWords);

        ArrayList<Word> list = new ArrayList<Word>();

        try{
            List<Word> topPopularWords = popularWords.subList(0, 10);
            for(Word item: topPopularWords) {
                list.add(item);
            }
        } catch (Exception e) {
//            System.out.println( "Size " + popularWords.size() );
        }

        return list;
    }


    public static ArrayList<Word> translate(ArrayList<Word> words)  {
//        TreeMap<String, String> dictionary = new TreeMap<String, String>();

        String[] array = getText(words);
        String[] translatedText = new String[1];

//            Translate.setClientId("69063100words");
//            Translate.setClientSecret("x8LVUEDcJINuNaOdY7w4uVtd1/FzK2S599HgL1ZD99s=");

            try {
//                translatedText = Translate.execute(array, Language.RUSSIAN, Language.ENGLISH);

            } catch (Exception e) {
                e.printStackTrace();
            }

        for(int i = 0; i < array.length; i++) {
//            dictionary.put(array[i], translatedText[i]);
//            dictionary.put(array[i], "translation");
            words.get(i).setTranslation("translation");
        }

        return words;
    }

    public static String[] getText(ArrayList<Word> words) {
        int size = words.size();
        String[] array = new String[size];
        for(int i = 0; i < size; i++) {
            array[i] = words.get(i).getText();
        }
        return array;
    }

//
//    public static void main(String[] args) {
//        String title = "<doc> <p> rогда со asd asdas dss sss ss s мной происходит </p> <p> что-то хорошее, это оказывает </p> </doc>";
//        EnglishLexicon lexicon = new EnglishLexicon();
//
//        System.out.println(lexicon.getTopHundred());
//
//
//        String sentences = getParagraphs(title);
//        System.out.println(sentences);
//        ArrayList<String> words = getWords(sentences);
//        words = deleteEasyWords(words,  lexicon.getTopHundred());
//        System.out.println(words);
//        ArrayList<Word> popularWords = getPopularWords(words);
//        System.out.println(popularWords);
//
//        for(int i = 0; i < popularWords.size(); i++) {
//        //    System.out.println(popularWords.get(i).getText());
//        }
//
//        ArrayList<Word> translatedWords = translate(popularWords);
//
//        for(int i = 0; i < popularWords.size(); i++) {
//            System.out.println(translatedWords.get(i).getText());
//        }
//
//        // generating Json
//        String string = "[";
////        for(Map.Entry<String,String> entry: dictionary.entrySet()) {
////            String key = entry.getKey();
////            String value = entry.getValue();
//        for(Word item: translatedWords) {
//            String key = item.getText();
//            String value = item.getTranslation();
//            int frequency = item.getFrequency();
//            string += " { \"key\" : \"" + key + "\" , \"value\" : \"" + value + ", \"frequency\" : \"" + frequency +  "\"  },";
//        }
//        string = string.substring(0, string.length() - 1);
//        string += "]";
//
//        System.out.println(string);
//
//    }
}