package parseBook;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by strapper on 02.09.15.
 */
public abstract class Book {

    private String notValidBook = "{ \"error\": \"notValidBook\" }";
    private int numberOfWordsToTranslate = 25;
    private byte[] input;
    private String text;
    private ArrayList<String> allWords;
    private ArrayList<Word> popularWords;
    private EnglishLexicon lexicon;

    private String textLanguage;
    private String textLanguageName;


    public abstract void parse(byte[] input);

    public Book(byte[] input, EnglishLexicon lexicon) {
        allWords = new ArrayList<String>();
        popularWords = new ArrayList<Word>();
        this.lexicon = lexicon;
        this.input = input;

    }

    public String getTextLanguage() {
        return textLanguageName;
    }

    public void findWords() throws Exception {
    //    System.out.println(text);
        ArrayList<String> words = new ArrayList<String>();
        String[] array = text.split(" ");
            for(String element: array) {
                String word = element.trim();
                word = StringUtils.strip(word, ",");
                word = StringUtils.strip(word, ".");
                word = StringUtils.strip(word, "\"");
                if(!word.equals("")) {
                    words.add(word);
                }
            }
        if(words.size() < 80) {
            throw new Exception();
        }

        allWords = words;
    }

    public void deleteEasyWords(HashSet<String> lexicon) {
        Iterator<String> iterator = allWords.iterator();
        while(iterator.hasNext()) {
            String item = iterator.next();
            if(lexicon.contains(item.toLowerCase())) {
                iterator.remove();
            }
        }
        System.out.println("after deletion remain : " + allWords.size());
    }

    public void deleteUncoventionalWords(HashSet<String> lexicon) {
        Iterator<String> iterator = allWords.iterator();
        while(iterator.hasNext()) {
            String item = iterator.next();
            if(! (lexicon.contains(item.toLowerCase()) || lexicon.contains(item)  )) {
                iterator.remove();
            }
        }
        System.out.println("after deletion remain : " + allWords.size());
    }



    public void findPopularWords() {

        TreeMap<String, Integer> wordsFrequency = new TreeMap<String, Integer>();  // key:words; value:times the word comes across in the book
        ArrayList<Word> listOfWords = new ArrayList<Word>();

        for(String item: allWords) {
            if(wordsFrequency.containsKey(item)) {
                int frequency = wordsFrequency.get(item);
                wordsFrequency.put(item, frequency + 1);
            } else {
                wordsFrequency.put(item, 1);
            }
        }

        for(Map.Entry<String,Integer> entry: wordsFrequency.entrySet()) {
            String wordText = entry.getKey();
            Integer frequency = entry.getValue();
            Word word = new Word(wordText, frequency);

            listOfWords.add(word);
        }

        Collections.sort(listOfWords); // sort by frequency

        try{
            for(int i = 0; i < numberOfWordsToTranslate; i++) {
                popularWords.add(listOfWords.get(i));
            }
        } catch (Exception e) {
//            System.out.println( "Size " + popularWords.size() );
        }

    }

    public void findBookLanguage() {
        Detect.setClientId("69063100words");
        Detect.setClientSecret("x8LVUEDcJINuNaOdY7w4uVtd1/FzK2S599HgL1ZD99s=");

        //Detect returns a Language Enum representing the language code
        try {
            Language detectedLanguage1 = Detect.execute(allWords.get(1) + " " + allWords.get(2) + " " + allWords.get(3));
            textLanguageName = detectedLanguage1.getName(Language.ENGLISH);
            textLanguage = detectedLanguage1.toString();
//            System.out.println(textLanguage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void translate(String outputLanguage)  {
        String[] array = getArrayOfWords();
        String[] translatedText = new String[1];
            Translate.setClientId("69063100words");
            Translate.setClientSecret("x8LVUEDcJINuNaOdY7w4uVtd1/FzK2S599HgL1ZD99s=");

//        Language lang = Language.UKRAINIAN;

        Language langInput = Language.fromString(textLanguage);
        Language langOutput = Language.fromString(outputLanguage);
        System.out.println(textLanguage + "    " + outputLanguage);

        try {
            translatedText = Translate.execute(array, langInput, langOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < array.length; i++) {
 //           popularWords.get(i).setTranslation("translation");
            popularWords.get(i).setTranslation(translatedText[i]);
        }
    }

    public void findDefinitions(HashMap<String, String> dictionary)  {
        for(Word item: popularWords) {
            String word = item.getText();
            String definition = lexicon.getDefinition(word.toLowerCase());
            item.setDefinition(definition);
        }
    }



    public String[] getArrayOfWords() {
        int size = popularWords.size();
        String[] array = new String[size];
        for(int i = 0; i < size; i++) {
            array[i] = popularWords.get(i).getText();
        }
        return array;
    }

    public String toJSON() {

        // generating Json
        String string = "[";

        for(Word item: popularWords) {
            String key = item.getText();
            String value = item.getTranslation();
            int frequency = item.getFrequency();
            String definition = item.getDefinition();

            string += " {\"key\":\"" + key + "\",\"value\":\"" + value + "\",\"frequency\":\"" +frequency+"\" , \"definition\" : \"" + definition + "\"},";

        }
        string = string.substring(0, string.length() - 1);
        string += "]";

        return string;
    }

    public String getJsonTranslation(String lang) {
        parse(input);
        try{
            findWords();
        } catch (Exception e) {
            return notValidBook;
        }
        findBookLanguage();

        if(textLanguage.equals("en")) {
            deleteEasyWords(lexicon.getEasyWords());
            deleteUncoventionalWords(lexicon.getEnglishLexicon());
        }

        findPopularWords();
        translate(lang);

        if(textLanguage.equals("en")) {
            findDefinitions(lexicon.getDefinitions());
        }
        String json = toJSON();
        return json;
    }


    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public ArrayList<String> getAllWords() {
        return allWords;
    }
    public void setAllWords(ArrayList<String> allWords) {
        this.allWords = allWords;
    }
    public ArrayList<Word> getPopularWords() {
        return popularWords;
    }
    public void setPopularWords(ArrayList<Word> popularWords) {
        this.popularWords = popularWords;
    }
    public byte[] getInput() {
        return input;
    }
    public void setInput(byte[] input) {
        this.input = input;
    }
}
