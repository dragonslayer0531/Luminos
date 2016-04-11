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
     * @param fontSize	Double of font size
     * 
     * Constructor
     */
    protected Word(double fontSize){
        this.fontSize = fontSize;
    }
     
    /**
     * @param character	Character to add
     * 
     * Adds character to word
     */
    protected void addCharacter(Character character){
        characters.add(character);
        width += character.getxAdvance() * fontSize;
    }
     
    /**
     * @return List<Character>	Characters in word
     * 
     * Gets characters in word
     */
    protected List<Character> getCharacters(){
        return characters;
    }
     
    /**
     * @return double	Width of word
     * 
     * Gets width of word
     */
    protected double getWordWidth(){
        return width;
    }

}
