package luminosutils.serialization;

import static luminosutils.serialization.SerializationUtils.readChars;
import static luminosutils.serialization.SerializationUtils.readInt;
import static luminosutils.serialization.SerializationUtils.readShort;
import static luminosutils.serialization.SerializationUtils.readString;
import static luminosutils.serialization.SerializationUtils.writeBytes;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Luminos String data
 *
 */

public class LString extends LBase {
	
	public static final byte CONTAINER_TYPE = ContainerType.STRING;
	public int count;
	private char[] characters;
	
	/**
	 * Private Constructor
	 */
	private LString() {
		size += 1 + 4;
	}
	
	/**
	 * Gets the string of the Luminos String
	 * 
	 * @return Luminos String characters
	 */
	public String getString() {
		return new String(characters);
	}
	
	/**
	 * 
	 */
	private void updateSize() {
		size += getDataSize();
	}
	
	/**
	 * Get the bytes of the Luminos String
	 * 
	 * @param dest		Destination byte array
	 * @param pointer	Integer of where to start writing
	 * @return			Final pointer location
	 */
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, count);
		pointer = writeBytes(dest, pointer, characters);
		return pointer;
	}
	
	/**
	 * Gets the size of the Luminos String
	 * 
	 * @return	size of the Luminos String
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the size of the Luminos String data
	 * 
	 * @return	size of the Luminos String data
	 */
	public int getDataSize() {
		return characters.length * Type.getSize(Type.CHAR);
	}
	
	/**
	 * Creates an Luminos String
	 * 
	 * @param name		Name of string data
	 * @param data		Value of data
	 * @return			Luminos String holding the data
	 */
	public static LString Create(String name, String data) {
		LString string = new LString();
		string.setName(name);
		string.count = data.length();
		string.characters = data.toCharArray();
		string.updateSize();
		return string;
	}
	
	/**
	 * Deserializes raw bytes into a Luminos String
	 * 
	 * @param data		Raw bytes to be deserialized
	 * @param pointer	Integer to start reading at
	 * @return			Luminos String to hold the raw data in
	 */
	public static LString Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		LString result = new LString();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		result.characters = new char[result.count];
		readChars(data, pointer, result.characters);
		
		pointer += result.count * Type.getSize(Type.CHAR);
		return result;
	}

}

