package tk.luminos.graphics.text;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Line of GUI Text
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Line {

	private double maxLength;
    private double spaceSize;
 
    private List<Word> words = new ArrayList<Word>();
    private double currentLineLength = 0;
 
    /**
     * Constructor
     * 
     * @param spaceWidth	Space width of line
     * @param fontSize		Font size of GUI Text
     * @param maxLength		Maximum length of line
     */
    protected Line(double spaceWidth, double fontSize, double maxLength) {
        this.spaceSize = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }
 
    /**
     * Attempts to add word to line
     * 
     * @param word	Word to be added
     */
    protected boolean attemptToAddWord(Word word) {
        double additionalLength = word.getWordWidth();
        additionalLength += !words.isEmpty() ? spaceSize : 0;
        if (currentLineLength + additionalLength <= maxLength) {
            words.add(word);
            currentLineLength += additionalLength;
            return true;
        } else {
            return false;
        }
    }
 
    /**
     * Gets max line length
     * 
     * @return	Max line length
     */
    protected double getMaxLength() {
        return maxLength;
    }
 
    /**
     * Gets the current line length
     * 
     * @return 	Current line length
     */
    protected double getLineLength() {
        return currentLineLength;
    }
 
    /**
     * Gets words in line
     * 
     * @return	Words in line
     */
    protected List<Word> getWords() {
        return words;
    }

}
