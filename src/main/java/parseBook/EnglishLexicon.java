package parseBook;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by strapper on 02.09.15.
 */
public class EnglishLexicon {

    private HashSet<String> easyWords;
    private HashSet<String> englishLexicon;
    private HashMap<String, String> definitions;

    final String hostName = "https://top-words.herokuapp.com/";
    final String hostName2 = "http://localhost:8081/";

    final String fileForBeginner = hostName + "top_100_popular_words.txt";
    final String fileForIntermediate = hostName + "top_1000_popular_words.txt";
    final String fileOfEnglishLexicon = hostName + "Canonical_English_lexicon.txt";
    final String fileOfDict = hostName + "dictionary.txt";


    public EnglishLexicon() {
        loadEasyWords(fileForIntermediate);
        loadLexicon(fileOfEnglishLexicon);
        loadDefinitionDict(fileOfDict);
    }


    public String getDefinition(String word) {
        String def = definitions.get(word);

        if(def == null && word.substring(word.length() - 3, word.length()).equals("ies")) {
            String newWord = word.substring(0, word.length() - 3) + "y";
            def = "#" + newWord + "# " + definitions.get(newWord);
        }

        if(def == null && word.charAt(word.length() - 1) == 's') {
            String newWord = word.substring(0, word.length() - 1);
            def = "#" + newWord + "# " + definitions.get(newWord);
        }

        if(def == null && word.substring(word.length() - 1, word.length()).equals("d")) {
            String newWord = word.substring(0, word.length() - 1);
            if(definitions.containsKey(newWord)) {
                def = "#" + newWord + "#  " + definitions.get(newWord);
            }

        }

        if(def == null && word.substring(word.length() - 2, word.length()).equals("ed")) {
            String newWord = word.substring(0, word.length() - 2);
            def = "#" + newWord + "#  " + definitions.get(newWord);
        }

        CharSequence cs = "*";
        if(def != null && def.contains(cs)) {
            int referenceStart = def.indexOf("*") + 1;
            int referenceEnd = 0;
            for(int i = referenceStart; i < def.length(); i++) {
                if(def.charAt(i) == '1' || def.charAt(i) == '.' || def.charAt(i) == ']' || def.charAt(i) == ' ' || def.charAt(i) == ',') {
                    referenceEnd = i;
                    break;
                }
            }
            String reference = def.substring(referenceStart, referenceEnd);
            def = def + " <br>" + "#" + reference + "#" + " - " + definitions.get(reference) ;
        }

        if(def == null) {
            def = "No definition found for this word";
        }

        return def;
    }



    public void loadLexicon(String fileName) {
        englishLexicon = loadSet(fileName);
    }
    public void loadEasyWords(String fileName) {
        easyWords = loadSet(fileName);
    }
    private HashSet<String> loadSet(String fileName) {
        HashSet<String> set = new HashSet<String>();
        URL oracle = null;
        try {
            oracle = new URL(fileName);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                set.add(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }
    public void loadDefinitionDict( String fileName) {
        HashMap<String, String> set = new HashMap<String, String>();

        URL oracle = null;
        try {
            oracle = new URL(fileName);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String arr[] = inputLine.split("::", 2);
                String firstWord = arr[0];   //first word
                String theRest = arr[1];     //the rest of the sentence
                set.put(firstWord, theRest);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        definitions = set;
    }

    public HashSet<String> getEasyWords() {
        return easyWords;
    }
    public HashSet<String> getEnglishLexicon() {
        return englishLexicon;
    }
    public HashMap<String, String> getDefinitions() {
        return definitions;
    }




}
