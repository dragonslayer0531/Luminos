package com.luminos.filesystem.serialization;

import static com.luminos.filesystem.serialization.SerializationUtils.readBooleans;
import static com.luminos.filesystem.serialization.SerializationUtils.readBytes;
import static com.luminos.filesystem.serialization.SerializationUtils.readChars;
import static com.luminos.filesystem.serialization.SerializationUtils.readDoubles;
import static com.luminos.filesystem.serialization.SerializationUtils.readFloats;
import static com.luminos.filesystem.serialization.SerializationUtils.readInt;
import static com.luminos.filesystem.serialization.SerializationUtils.readInts;
import static com.luminos.filesystem.serialization.SerializationUtils.readLongs;
import static com.luminos.filesystem.serialization.SerializationUtils.readShort;
import static com.luminos.filesystem.serialization.SerializationUtils.readShorts;
import static com.luminos.filesystem.serialization.SerializationUtils.readString;
import static com.luminos.filesystem.serialization.SerializationUtils.writeBytes;
	
/**
 * 
 * Luminos Array Object
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LArray extends LBase {

	public static final byte CONTAINER_TYPE = ContainerType.ARRAY;
	public byte type;
	public int count;
	public byte[] data;
	
	private short[] shortData;
	private char[] charData;
	private int[] intData;
	private long[] longData;
	private float[] floatData;
	private double[] doubleData;
	private boolean[] booleanData;
	
	/**
	 * Private Constructor
	 */
	private LArray() {
		size += 1 + 1 + 4;
	}
	
	/**
	 * Updates the size of the array
	 */
	private void updateSize() {
		size += getDataSize();
	}
	
	/**
	 * Writes the array to a byte array
	 * 
	 * @param dest		Destination byte array
	 * @param pointer	Pointer to start the write cycle at
	 * @return			Pointer location at end of write cycle
	 */
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, count);
		
		switch(type) {
		case Type.BYTE:
			pointer = writeBytes(dest, pointer, data);
			break;
		case Type.SHORT:
			pointer = writeBytes(dest, pointer, shortData);
			break;
		case Type.CHAR:
			pointer = writeBytes(dest, pointer, charData);
			break;
		case Type.INTEGER:
			pointer = writeBytes(dest, pointer, intData);
			break;
		case Type.LONG:
			pointer = writeBytes(dest, pointer, longData);
			break;
		case Type.FLOAT:
			pointer = writeBytes(dest, pointer, floatData);
			break;
		case Type.DOUBLE:
			pointer = writeBytes(dest, pointer, doubleData);
			break;
		case Type.BOOLEAN:
			pointer = writeBytes(dest, pointer, booleanData);
			break;
		}
		return pointer;
	}
	
	/**
	 * Gets the size of the array
	 * 
	 * @return 	size of the array
	 */
	public int getSize() {
		return size;
	}
	/**
	 * Gets the size of the data type
	 * @return	size of the data type
	 */
	public int getDataSize() {
		switch(type) {
		case Type.BYTE:		return data.length * Type.getSize(Type.BYTE);
		case Type.SHORT:	return shortData.length * Type.getSize(Type.SHORT);
		case Type.CHAR:		return charData.length * Type.getSize(Type.CHAR);
		case Type.INTEGER:	return intData.length * Type.getSize(Type.INTEGER);
		case Type.LONG:		return longData.length * Type.getSize(Type.LONG);
		case Type.FLOAT:	return floatData.length * Type.getSize(Type.FLOAT);
		case Type.DOUBLE:	return doubleData.length * Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN:	return booleanData.length * Type.getSize(Type.BOOLEAN);
		}
		return 0;
	}
	
	/**
	 * Writes bytes to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Byte(String name, byte[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.BYTE;
		array.count = data.length;
		array.data = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes shorts to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Short(String name, short[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.SHORT;
		array.count = data.length;
		array.shortData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes chars to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Char(String name, char[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.CHAR;
		array.count = data.length;
		array.charData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes integers to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Integer(String name, int[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.INTEGER;
		array.count = data.length;
		array.intData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes longs to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Long(String name, long[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.LONG;
		array.count = data.length;
		array.longData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes floats to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Float(String name, float[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.FLOAT;
		array.count = data.length;
		array.floatData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes doubles to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Double(String name, double[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.DOUBLE;
		array.count = data.length;
		array.doubleData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Writes booleans to a Luminos Array
	 * 
	 * @param name	Name of the array
	 * @param data	Data to be written
	 * @return		LArray that was written to
	 */
	public static LArray Boolean(String name, boolean[] data) {
		LArray array = new LArray();
		array.setName(name);
		array.type = Type.BOOLEAN;
		array.count = data.length;
		array.booleanData = data;
		array.updateSize();
		return array;
	}
	
	/**
	 * Deserializes raw bytes into a Luminos Array
	 * 
	 * @param data		Raw data to deserialize
	 * @param pointer	Pointer to begin deserialization at
	 * @return			LArray holding deserialized data
	 */
	public static LArray Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		LArray result = new LArray();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.type = data[pointer++];
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		switch(result.type) {
		case Type.BYTE:
			result.data = new byte[result.count];
			readBytes(data, pointer, result.data);
			break;
		case Type.SHORT:
			result.shortData = new short[result.count];
			readShorts(data, pointer, result.shortData);
			break;
		case Type.CHAR:
			result.charData = new char[result.count];
			readChars(data, pointer, result.charData);
			break;
		case Type.INTEGER:
			result.intData = new int[result.count];
			readInts(data, pointer, result.intData);
			break;
		case Type.LONG:
			result.longData = new long[result.count];
			readLongs(data, pointer, result.longData);
			break;
		case Type.FLOAT:
			result.floatData = new float[result.count];
			readFloats(data, pointer, result.floatData);
			break;
		case Type.DOUBLE:
			result.doubleData = new double[result.count];
			readDoubles(data, pointer, result.doubleData);
			break;
		case Type.BOOLEAN:
			result.booleanData = new boolean[result.count];
			readBooleans(data, pointer, result.booleanData);
			break;
		}
		
		pointer += result.count * Type.getSize(result.type);
		
		return result;
	}
	
}

