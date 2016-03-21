package luminoscore.graphics.text;

import java.util.ArrayList;
import java.util.List;

public class Line {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */

	//Constructor Data
	private double maxLength;
	private double spaceSize;

	private List<Word> words = new ArrayList<Word>();
	private double currentLineLength = 0;

	/*
	 * @param spaceWidth The width of a space character
	 * @param fontSize Sets font size
	 * @param maxLength Defines the maximum length of a character
	 * 
	 * Constructor
	 */
	protected Line(double spaceWidth, double fontSize, double maxLength) {
		this.spaceSize = spaceWidth * fontSize;
		this.maxLength = maxLength;
	}

	/*
	 * @param word Defines Word to be added
	 * 
	 * Adds word to line if possible
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

	//Protected Getter Methods
	protected double getMaxLength() {
		return maxLength;
	}

	protected double getLineLength() {
		return currentLineLength;
	}

	protected List<Word> getWords() {
		return words;
	}

}
