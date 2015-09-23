package parseBook;

import org.xml.sax.InputSource;

import java.io.*;
import java.util.HashSet;

/**
 * Created by strapper on 03.09.15.
 */
public class TXTBook extends Book {

    public TXTBook(byte[]  input, EnglishLexicon lexicon) {
        super(input, lexicon);
    }

    @Override
    public void parse(byte[]  input) {
        String text = "";

        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(input);
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(byteStream,"UTF-8"));

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                text += sCurrentLine;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setText(text);
    }
}
