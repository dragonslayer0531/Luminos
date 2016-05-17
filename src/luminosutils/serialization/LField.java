package luminosutils.serialization;

import static luminosutils.serialization.SerializationUtils.*;

public class LField extends Base {
	
	public static final byte CONTAINER_TYPE = ContainerType.FIELD;
	public byte type;
	public byte[] data;
	
	public LField() {
		
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
	
	public static LField Byte(String name, byte value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.BYTE;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Short(String name, short value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.SHORT;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Char(String name, char value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.CHAR;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Integer(String name, int value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.INTEGER;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Long(String name, long value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.LONG;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Float(String name, float value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.FLOAT;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Double(String name, double value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.DOUBLE;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static LField Boolean(String name, boolean value) {
		LField field = new LField();
		field.setName(name);
		field.type = Type.BOOLEAN;
		field.data = new byte[Type.getSize(field.type)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
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
