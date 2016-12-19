package com.luminos.filesystem.serialization;

import static com.luminos.filesystem.serialization.SerializationUtils.readInt;
import static com.luminos.filesystem.serialization.SerializationUtils.readShort;
import static com.luminos.filesystem.serialization.SerializationUtils.readString;
import static com.luminos.filesystem.serialization.SerializationUtils.writeBytes;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Luminos Object data
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LObject extends LBase {

	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	private short fieldCount;
	public List<LField> fields = new ArrayList<LField>();
	private short stringCount;
	public List<LString> strings = new ArrayList<LString>();
	private short arrayCount;
	public List<LArray> arrays = new ArrayList<LArray>();
	
	/**
	 * Private constructor
	 */
	private LObject() {
	}
	
	/**
	 * Constructor
	 * 
	 * @param name	Name of object
	 */
	public LObject(String name) {
		size += 1 + 2 + 2 + 2;
		setName(name);
	}
	
	/**
	 * Adds {@link LField} to Luminos Object
	 * 
	 * @param field		Luminos field to be added
	 */
	public void addField(LField field) {
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short)fields.size();
	}
	
	/**
	 * Adds {@link LString} to Luminos Object
	 * 
	 * @param string	Luminos String to be added
	 */
	public void addString(LString string) {
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short)strings.size();
	}

	/**
	 * Adds {@link LArray} to Luminos Object
	 * 
	 * @param array		Luminos Array to be added
	 */
	public void addArray(LArray array) {
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short)arrays.size();
	}
	
	/**
	 * Gets size of the Luminos Object
	 * 
	 * @return size of the Luminos Object
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the {@link LField} via its name
	 * 
	 * @param name	Name to search for
	 * @return		Luminos Field with matching name
	 */
	public LField findField(String name) {
		for (LField field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	/**
	 * Gets the {@link LString} via its name
	 * 
	 * @param name	Name to search for
	 * @return		Luminos String with matching name
	 */
	public LString findString(String name) {
		for (LString string : strings) {
			if (string.getName().equals(name))
				return string;
		}
		return null;
	}

	/**
	 * Gets the {@link LArray} via its name
	 * 
	 * @param name	Name to search for
	 * @return		Luminos Array with matching name
	 */
	public LArray findArray(String name) {
		for (LArray array : arrays) {
			if (array.getName().equals(name))
				return array;
		}
		return null;
	}
	
	/**
	 * Gets bytes of Luminos Object
	 * 
	 * @param dest		Destination byte array to write to
	 * @param pointer	Integer to begin reading at
	 * @return			Final pointer location
	 */
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for (LField field : fields)
			pointer = field.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, stringCount);
		for (LString string : strings)
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (LArray array : arrays)
			pointer = array.getBytes(dest, pointer);
		
		return pointer;
	}
	
	/**
	 * Deserializes byte to Luminos Object
	 * 
	 * @param data		Data to deserialize from
	 * @param pointer	Integer to begin reading from
	 * @return			Luminos Object deserialized to
	 */
	public static LObject Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		LObject result = new LObject();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		// Early-out: pointer += result.size - sizeOffset - result.nameLength;
		
		result.fieldCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.fieldCount; i++) {
			LField field = LField.Deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.stringCount; i++) {
			LString string = LString.Deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}

		result.arrayCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.arrayCount; i++) {
			LArray array = LArray.Deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
	
}

