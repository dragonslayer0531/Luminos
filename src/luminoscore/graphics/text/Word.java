package luminoscore.graphics.text;

import java.util.ArrayList;
import java.util.List;

public class Word {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */
	
	//Data
	private List<Character> characters = new ArrayList<Character>();
	private double width = 0;
	private double fontSize;
	
	/*
	 * @param fontSize defines the size of the word
	 * 
	 * Constructor
	 */
	protected Word(double fontSize) {
		this.fontSize = fontSize;
	}
	
	/*
	 * @ param character Defines character to be added
	 * 
	 * Adds character to word
	 */
	
	protected void addCharacter(Character character) {
		characters.add(character);
		width += character.getxAdvance() * fontSize;
	}
	
	//Protected Getter Methods
	protected List<Character> getCharacters() {
		return characters;
	}
	
	protected double getWordWidth() {
		return width;
	}

}
