package parseBook;

import java.util.HashSet;

/**
 * Created by strapper on 03.09.15.
 */
public class TXTBook extends Book {

    public TXTBook(String input, EnglishLexicon lexicon) {
        super(input, lexicon);
    }

    @Override
    public void parse(String input) {
        setText(input);
    }


}
