package parseBook;

/**
 * Created by strapper on 25.08.15.
 */
public class Word implements Comparable {

    private String text;
    private String translation;
    private String definition;
    private int frequency;

    public Word(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public String getDefinition() {
        return definition;
    }
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }


    public int compareTo(Object o) {
        Word other = (Word) o;
        if(this.getFrequency() < other.getFrequency()) {
            return 1;
        } else if(this.getFrequency() > other.getFrequency()) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return text + " : " + translation + " : " + frequency ;
    }
}
