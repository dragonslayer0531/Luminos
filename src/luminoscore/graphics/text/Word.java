package luminoscore.graphics.text;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Word of GUI Text
 *
 */

public class Word {
	
	private List<Character> characters = new ArrayList<Character>();
    private double width = 0;
    private double fontSize;
     
    /**
     * Constructor
     * 
     * @param fontSize	Double of font size
     */
    protected Word(double fontSize){
        this.fontSize = fontSize;
    }
     
    /**
     * Adds character to word
     * 
     * @param character	Character to add
     */
    protected void addCharacter(Character character){
        characters.add(character);
        width += character.getxAdvance() * fontSize;
    }
     
    /**
     * Gets characters in word
     * 
     * @return Characters in word
     */
    protected List<Character> getCharacters(){
        return characters;
    }
     
    /**
     * Gets width of word
     * 
     * @return	Width of word
     */
    protected double getWordWidth(){
        return width;
    }

}
