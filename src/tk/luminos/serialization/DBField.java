package tk.luminos.serialization;

import static tk.luminos.serialization.SerializationUtils.readBoolean;
import static tk.luminos.serialization.SerializationUtils.readBytes;
import static tk.luminos.serialization.SerializationUtils.readChar;
import static tk.luminos.serialization.SerializationUtils.readDouble;
import static tk.luminos.serialization.SerializationUtils.readFloat;
import static tk.luminos.serialization.SerializationUtils.readInt;
import static tk.luminos.serialization.SerializationUtils.readLong;
import static tk.luminos.serialization.SerializationUtils.readShort;
import static tk.luminos.serialization.SerializationUtils.readString;
import static tk.luminos.serialization.SerializationUtils.writeBytes;

public class DBField extends DBBase {

	public static final byte CONTAINER_TYPE = ContainerType.FIELD;
	public byte type;
	public byte[] data;
	
	private DBField() {
	}
	
	public byte getByte() {
		return data[0];
	}
	
	public short getShort() {
		return readShort(data, 0);
	}
	
	public char getChar() {
		return readChar(data, 0);
	}
	
	public int getInt() {
		return readInt(data, 0);
	}
	
	public long getLong() {
		return readLong(data, 0);
	}
	
	public double getDouble() {
		return readDouble(data, 0);
	}
	
	public float getFloat() {
		return readFloat(data, 0);
	}
	
	public boolean getBoolean() {
		return readBoolean(data, 0);
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, data);
		return pointer;
	}
	
	public int getSize() {
		assert(data.length == Type.getSize(type));
		return 1 + 2 + name.length + 1 + data.length;
	}

	public static DBField createByteField(String name, byte value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.BYTE;
		field.data = new byte[Type.getSize(Type.BYTE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField createShortField(String name, short value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.SHORT;
		field.data = new byte[Type.getSize(Type.SHORT)];
		writeBytes(field.data, 0, value);
		return field;
	}

	public static DBField createCharacterField(String name, char value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.CHAR;
		field.data = new byte[Type.getSize(Type.CHAR)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField createIntegerField(String name, int value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.INTEGER;
		field.data = new byte[Type.getSize(Type.INTEGER)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField createLongField(String name, long value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.LONG;
		field.data = new byte[Type.getSize(Type.LONG)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField createFloatField(String name, float value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.FLOAT;
		field.data = new byte[Type.getSize(Type.FLOAT)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField createDoubleField(String name, double value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.DOUBLE;
		field.data = new byte[Type.getSize(Type.DOUBLE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField createBooleanField(String name, boolean value) {
		DBField field = new DBField();
		field.setName(name);
		field.type = Type.BOOLEAN;
		field.data = new byte[Type.getSize(Type.BOOLEAN)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static DBField deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		DBField result = new DBField();
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
