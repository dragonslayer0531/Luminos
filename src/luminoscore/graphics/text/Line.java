package luminoscore.graphics.text;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Line of GUI Text
 *
 */

public class Line {

	private double maxLength;
    private double spaceSize;
 
    private List<Word> words = new ArrayList<Word>();
    private double currentLineLength = 0;
 
    /**
     * @param spaceWidth	Space width of line
     * @param fontSize		Font size of GUI Text
     * @param maxLength		Maximum length of line
     * 
     * Constructor
     */
    protected Line(double spaceWidth, double fontSize, double maxLength) {
        this.spaceSize = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }
 
    /**
     * @param word	Word to be added
     * 
     * Attempts to add word to line
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
     * @return double	Max line length
     * 
     * Gets max line length
     */
    protected double getMaxLength() {
        return maxLength;
    }
 
    /**
     * @return double	Current line length
     * 
     * Gets the current line length
     */
    protected double getLineLength() {
        return currentLineLength;
    }
 
    /**
     * @return List<Word>	Words in line
     * 
     * Gets words in line
     */
    protected List<Word> getWords() {
        return words;
    }

}
