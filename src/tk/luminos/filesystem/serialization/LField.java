package tk.luminos.filesystem.serialization;

import static tk.luminos.filesystem.serialization.SerializationUtils.readBoolean;
import static tk.luminos.filesystem.serialization.SerializationUtils.readBytes;
import static tk.luminos.filesystem.serialization.SerializationUtils.readChar;
import static tk.luminos.filesystem.serialization.SerializationUtils.readDouble;
import static tk.luminos.filesystem.serialization.SerializationUtils.readFloat;
import static tk.luminos.filesystem.serialization.SerializationUtils.readInt;
import static tk.luminos.filesystem.serialization.SerializationUtils.readLong;
import static tk.luminos.filesystem.serialization.SerializationUtils.readShort;
import static tk.luminos.filesystem.serialization.SerializationUtils.readString;
import static tk.luminos.filesystem.serialization.SerializationUtils.writeBytes;

/**
 * 
 * Holds field data
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LField extends LBase {

	public static final byte CONTAINER_TYPE = ContainerType.FIELD;
	public byte type;
	public byte[] data;
	
	/**
	 * Private Constructor
	 */
	private LField() {
	}
	
	/**
	 * Gets the byte ID of the field
	 * 
	 * @return	byte ID
	 */
	public byte getByte() {
		return data[0];
	}
	
	/**
	 * Gets short data of field
	 * 
	 * @return		Field data
	 */
	public short getShort() {
		return readShort(data, 0);
	}

	/**
	 * Gets char data of field
	 * 
	 * @return		Field data
	 */
	public char getChar() {
		return readChar(data, 0);
	}
	
	/**
	 * Gets integer data of field
	 * 
	 * @return		Field data
	 */
	public int getInt() {
		return readInt(data, 0);
	}
	
	/**
	 * Gets long data of field
	 * 
	 * @return		Field data
	 */
	public long getLong() {
		return readLong(data, 0);
	}
	
	/**
	 * Gets double data of field
	 * 
	 * @return		Field data
	 */
	public double getDouble() {
		return readDouble(data, 0);
	}
	
	/**
	 * Gets float data of field
	 * 
	 * @return		Field data
	 */
	public float getFloat() {
		return readFloat(data, 0);
	}
	
	/**
	 * Gets boolean data of field
	 * 
	 * @return		Field data
	 */
	public boolean getBoolean() {
		return readBoolean(data, 0);
	}
	
	/**
	 * Gets the bytes of the Luminos field
	 * 
	 * @param dest		Destination byte array
	 * @param pointer	Integer at where to begin writing at
	 * @return			Final pointer location
	 */
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, data);
		return pointer;
	}
	
	/**
	 * Gets the size of the Luminos Field
	 * 
	 * @return 	size of the Luminos Field
	 */
	public int getSize() {
		assert(data.length == Type.getSize(type));
		return 1 + 2 + name.length + 1 + data.length;
	}

	/**
	 * Creates a Luminos field with a byte
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Byte(String name, byte value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.BYTE;
		field.data = new byte[Type.getSize(Type.BYTE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Creates a Luminos field with a short
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Short(String name, short value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.SHORT;
		field.data = new byte[Type.getSize(Type.SHORT)];
		writeBytes(field.data, 0, value);
		return field;
	}

	/**
	 * Creates a Luminos field with a char
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Char(String name, char value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.CHAR;
		field.data = new byte[Type.getSize(Type.CHAR)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Creates a Luminos field with a integer
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Integer(String name, int value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.INTEGER;
		field.data = new byte[Type.getSize(Type.INTEGER)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Creates a Luminos field with a long
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Long(String name, long value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.LONG;
		field.data = new byte[Type.getSize(Type.LONG)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Creates a Luminos field with a float
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Float(String name, float value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.FLOAT;
		field.data = new byte[Type.getSize(Type.FLOAT)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Creates a Luminos field with a double
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Double(String name, double value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.DOUBLE;
		field.data = new byte[Type.getSize(Type.DOUBLE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Creates a Luminos field with a boolean
	 * 
	 * @param name		Name of the field
	 * @param value		Value of the field
	 * @return			Luminos field of the value
	 */
	public static LField Boolean(String name, boolean value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.BOOLEAN;
		field.data = new byte[Type.getSize(Type.BOOLEAN)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	/**
	 * Deserializes Luminos Field from bytes
	 * @param data		Byte array to deserialize from
	 * @param pointer	Integer to begin reading at
	 * @return			Luminos Field to deserialize to
	 */
	public static LField Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		LField result = new LField();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.type = data[pointer++];
		
		result.data = new byte[Type.getSize(result.type)];
		readBytes(data, pointer, result.data);
		pointer += Type.getSize(result.type);
		return result;
	}
	
}

