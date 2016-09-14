package tk.luminos.graphics.opengl.text;

import static tk.luminos.ConfigData.HEIGHT;
import static tk.luminos.ConfigData.WIDTH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tk.luminos.Debug;

/**
 * 
 * Meta file of font
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class MetaFile {
	
	private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;
 
    private static final int DESIRED_PADDING = 1;
 
    private static final String SPLITTER = " ";
    private static final String NUMBER_SEPARATOR = ",";
 
    private double aspectRatio;
 
    private double verticalPerPixelSize;
    private double horizontalPerPixelSize;
    private double spaceWidth;
    private int[] padding;
    private int paddingWidth;
    private int paddingHeight;
 
    private Map<Integer, Character> metaData = new HashMap<Integer, Character>();
 
    private BufferedReader reader;
    private Map<String, String> values = new HashMap<String, String>();
 
    /**
     * Constructor
     * 
     * @param file		File to be loaded
     */
    protected MetaFile(File file) {
        this.aspectRatio = (double) WIDTH / (double) HEIGHT;
        openFile(file);
        loadPaddingData();
        loadLineSizes();
        int imageWidth = getValueOfVariable("scaleW");
        loadCharacterData(imageWidth);
        close();
    }
 
    /**
     * Gets space width
     * 
     * @return	Space width
     */
    protected double getSpaceWidth() {
        return spaceWidth;
    }
 
    /**
     * Gets character
     * 
     * @param ascii	Character's ASCII ID
     * @return	Character to be retreived
     */
    protected Character getCharacter(int ascii) {
        return metaData.get(ascii);
    }
 
//******************************************Private Methods*****************************************//
    
    /**
     * Gets and processes line
     * 
     * @return Is line processable
     */
    private boolean processNextLine() {
        values.clear();
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
        	Debug.addData(e);
        	Debug.print();
        }
        if (line == null) {
            return false;
        }
        for (String part : line.split(SPLITTER)) {
            String[] valuePairs = part.split("=");
            if (valuePairs.length == 2) {
                values.put(valuePairs[0], valuePairs[1]);
            }
        }
        return true;
    }
 
    /**
     * Gets	value of variable
     * 
     * @param variable	Variable to get
     * @return 			Variable value
     */
    private int getValueOfVariable(String variable) {
        return Integer.parseInt(values.get(variable));
    }
 
    /**
     * Gets value of variables
     * 
     * @param variable	Variable to get
     * @return		Variable values
     */
    private int[] getValuesOfVariable(String variable) {
        String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
        int[] actualValues = new int[numbers.length];
        for (int i = 0; i < actualValues.length; i++) {
            actualValues[i] = Integer.parseInt(numbers[i]);
        }
        return actualValues;
    }
 
    /**
     * Closes the font file after finishing reading.
     */
    private void close() {
        try {
            reader.close();
        } catch (IOException e) {
            Debug.addData(e);
            Debug.print();
        }
    }
 
    /**
     * Opens file
     * 
     * @param file	File to open
     */
    private void openFile(File file) {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
        	Debug.addData(e);
        	Debug.print();
        }
    }
 
    /**
     * Loads the data about how much padding is used around each character in the texture atlas.
     */
    private void loadPaddingData() {
        processNextLine();
        this.padding = getValuesOfVariable("padding");
        this.paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
        this.paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM];
    }
 
    /**
     * Loads line sizes to MetaFile
     */
    private void loadLineSizes() {
        processNextLine();
        int lineHeightPixels = getValueOfVariable("lineHeight") - paddingHeight;
        verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT / (double) lineHeightPixels;
        horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
    }
 
    /**
     * Loads character data to meta file
     * 
     * @param imageWidth	Width of Font File Image
     */
    private void loadCharacterData(int imageWidth) {
        processNextLine();
        processNextLine();
        while (processNextLine()) {
            Character c = loadCharacter(imageWidth);
            if (c != null) {
                metaData.put(c.getId(), c);
            }
        }
    }
 
    /**
     * Gets Character from ID file
     * 
     * @param imageSize		Size of font file image
     * @return Character	Character data
     */
    private Character loadCharacter(int imageSize) {
        int id = getValueOfVariable("id");
        if (id == TextMeshCreator.SPACE_ASCII) {
            this.spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
            return null;
        }
        double xTex = ((double) getValueOfVariable("x") + (padding[PAD_LEFT] - DESIRED_PADDING)) / imageSize;
        double yTex = ((double) getValueOfVariable("y") + (padding[PAD_TOP] - DESIRED_PADDING)) / imageSize;
        int width = getValueOfVariable("width") - (paddingWidth - (2 * DESIRED_PADDING));
        int height = getValueOfVariable("height") - ((paddingHeight) - (2 * DESIRED_PADDING));
        double quadWidth = width * horizontalPerPixelSize;
        double quadHeight = height * verticalPerPixelSize;
        double xTexSize = (double) width / imageSize;
        double yTexSize = (double) height / imageSize;
        double xOff = (getValueOfVariable("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize;
        double yOff = (getValueOfVariable("yoffset") + (padding[PAD_TOP] - DESIRED_PADDING)) * verticalPerPixelSize;
        double xAdvance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
        return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance);
    }

}
