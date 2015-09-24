package parseBook;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by strapper on 02.09.15.
 */
public abstract class Book {

    private String NOT_VALID_BOOK = "{ \"error\": \"NOT_VALID_BOOK\" }";    // error message for the case when the input file is too short
    private int numberOfWordsToTranslate = 25;
    private byte[] input;   // book in the binary format
    private String text;    // raw text from the book
    private ArrayList<String> allWords;     // list of all words of the book
    private ArrayList<Word> popularWords;   // list of all top most frequent words of the book; size= numberOfWordsToTranslate
    private EnglishLexicon lexicon;

    private String textLanguage;        // abbreviation         e.g. en
    private String textLanguageName;    // name of the Language e.g. English


    /**
     * Gets raw text form the book
     * @param input - book in the binary format
     */
    public abstract void parse(byte[]  input);

    public Book(byte[]  input, EnglishLexicon lexicon) {
        allWords = new ArrayList<String>();
        popularWords = new ArrayList<Word>();
        this.lexicon = lexicon;
        this.input = input;
    }

    /**
     * Returns Language, which book is written in
     */
    public String getTextLanguage() {
        return textLanguageName;
    }

    /**
     * Finds all words from the raw text of a book and puts them to allWords-ArrayList<String>
     */
    public void findWords() throws Exception {
        String[] array = text.split(" ");
            for(String element: array) {
                String word = element.trim();
                word = StringUtils.strip(word, ",");
                word = StringUtils.strip(word, ".");
                word = StringUtils.strip(word, "\"");
                if(!word.equals("")) {
                    allWords.add(word);
                }
            }
        if(allWords.size() < numberOfWordsToTranslate) {
            throw new Exception();
        }
    }

    /**
     * Deletes those words from allWords-ArrayList<String> that are in lexicon
     *  @param  lexicon  - set of easy words
     */
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

    /**
     * Deletes all the words that are beyond lexicon
     * @param  lexicon  - set of all valid English words
     */
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



    /**
     * Selects top - numberOfWordsToTranslate words from allWords list and puts them into popularWords list
     */
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

        }

    }

    /**
     * Finds the language of a book and puts into textLanguageName
     */
    public void findBookLanguage() {
        Detect.setClientId("69063100words");
        Detect.setClientSecret("x8LVUEDcJINuNaOdY7w4uVtd1/FzK2S599HgL1ZD99s=");
        try {
            Language detectedLanguage1 = Detect.execute(allWords.get(1) + " " + allWords.get(2) + " " + allWords.get(3));
            textLanguageName = detectedLanguage1.getName(Language.ENGLISH);
            textLanguage = detectedLanguage1.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Translates each word from list of popularWords into outputLanguage
     * @param outputLanguage - language to translate the book to
     */
    public void translate(String outputLanguage)  {
        String[] array = getArrayOfWords();
        String[] translatedText = new String[1];
        Translate.setClientId("69063100words");
        Translate.setClientSecret("x8LVUEDcJINuNaOdY7w4uVtd1/FzK2S599HgL1ZD99s=");

        Language langInput = Language.fromString(textLanguage);
        Language langOutput = Language.fromString(outputLanguage);
        System.out.println(textLanguage + "    " + outputLanguage);

        try {
            translatedText = Translate.execute(array, langInput, langOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < array.length; i++) {
            popularWords.get(i).setTranslation(translatedText[i]);
        }
    }


    /**
     * Finds definitions for each word of the list of popularWords
     * @param dictionary - dictionary of definitions (key - english word, value - definition of the key)
     */
    public void findDefinitions(HashMap<String, String> dictionary)  {
        for(Word item: popularWords) {
            String word = item.getText();
            String definition = lexicon.getDefinition(word.toLowerCase());
            item.setDefinition(definition);
        }
    }

    /**
     * Turns the list of popularWords into array
     * @return array of popularWords
     */
    public String[] getArrayOfWords() {
        int size = popularWords.size();
        String[] array = new String[size];
        for(int i = 0; i < size; i++) {
            array[i] = popularWords.get(i).getText();
        }
        return array;
    }

    public void deleteRepeatedWords() {
        Iterator<Word> iter = popularWords.iterator();
        while (iter.hasNext()){
            Word item = iter.next();
            String cand1 = item.getText().toLowerCase();
            String word =  item.getText();
            String cand2 = word.substring(0, 1).toUpperCase() + word.substring(1,word.length());
            String cand3 = lexicon.generateCandiadete(word);
            if(popularWords.contains(cand1) || popularWords.contains(cand2) || popularWords.contains(cand3)) {
                iter.remove();
            }
        }
    }

    /**
     * Creates json which contains:
     *          1. the textLanguageName ( language the book is written in )
     *          2. array of the most popular words with translation,defintion and word frequency
     * @return json
     */
    public String toJSON()  {
        try{
            JSONArray array = new JSONArray();
            for(Word item: popularWords) {
                String key = item.getText();
                String value = item.getTranslation();
                int frequency = item.getFrequency();
                String definition = item.getDefinition();

                JSONObject obj = new JSONObject();
                obj.put("key", key);
                obj.put("value", value);
                obj.put("frequency", frequency);
                if(definition == null) {
                    obj.put("definition", "null");
                } else {
                    obj.put("definition", definition);
                }
                array.put(obj);
            }

            JSONObject object = new JSONObject();
            object.put("language", this.getTextLanguage());
            object.put("contents", array);
            return object.toString();
        } catch (JSONException jsonE) {
            return null;
        }
    }

    /**
     * Searches for most popular words in the book, translates them and creates json as result
     * @param lang - language the book should be translated to
     * @return json
     */
    public String getJsonTranslation(String lang) {
        parse(input);
        try{
            findWords();
        } catch (Exception e) {
            return NOT_VALID_BOOK;
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
    public byte[]  getInput() {
        return input;
    }
    public void setInput(byte[]  input) {
        this.input = input;
    }
}
