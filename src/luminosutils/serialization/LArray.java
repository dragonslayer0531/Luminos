package luminosutils.serialization;

import static luminosutils.serialization.SerializationUtils.readBooleans;
import static luminosutils.serialization.SerializationUtils.readBytes;
import static luminosutils.serialization.SerializationUtils.readChars;
import static luminosutils.serialization.SerializationUtils.readDoubles;
import static luminosutils.serialization.SerializationUtils.readFloats;
import static luminosutils.serialization.SerializationUtils.readInt;
import static luminosutils.serialization.SerializationUtils.readInts;
import static luminosutils.serialization.SerializationUtils.readLongs;
import static luminosutils.serialization.SerializationUtils.readShort;
import static luminosutils.serialization.SerializationUtils.readShorts;
import static luminosutils.serialization.SerializationUtils.readString;
import static luminosutils.serialization.SerializationUtils.writeBytes;

public class LArray extends Base {
	
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
	
	public LArray() {
		size += 1 + 1 + 4;
	}
	
	private void updateSize() {
		size += getDataSize();
	}
	
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
	
	public int getSize() {
		return size;
	}
	
	public int getDataSize() {
		switch(type) {
		case Type.BYTE:		return data.length * Type.getSize(Type.BYTE);
		case Type.SHORT: 	return data.length * Type.getSize(Type.SHORT);
		case Type.CHAR:		return data.length * Type.getSize(Type.CHAR);
		case Type.INTEGER:	return data.length * Type.getSize(Type.INTEGER);
		case Type.LONG:		return data.length * Type.getSize(Type.LONG);
		case Type.FLOAT:	return count * Type.getSize(Type.FLOAT);
		case Type.DOUBLE:	return data.length * Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN:	return data.length * Type.getSize(Type.BOOLEAN);
		}
		return 0;
	}
	
	public static LArray Byte(String name, byte[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.BYTE;
		LArray.count = data.length;
		LArray.data = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Short(String name, short[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.SHORT;
		LArray.count = data.length;
		LArray.shortData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Char(String name, char[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.CHAR;
		LArray.count = data.length;
		LArray.charData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Integer(String name, int[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.INTEGER;
		LArray.count = data.length;
		LArray.intData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Long(String name, long[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.LONG;
		LArray.count = data.length;
		LArray.longData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Float(String name, float[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.FLOAT;
		LArray.count = data.length;
		LArray.floatData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Double(String name, double[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.DOUBLE;
		LArray.count = data.length;
		LArray.doubleData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Boolean(String name, boolean[] data) {
		LArray LArray = new LArray();
		LArray.setName(name);
		LArray.type = Type.BOOLEAN;
		LArray.count = data.length;
		LArray.booleanData = data;
		LArray.updateSize();
		return LArray;
	}
	
	public static LArray Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer]++;
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
			result.data = new byte[result.count];
			readShorts(data, pointer, result.shortData);
			break;
		case Type.CHAR:
			result.data = new byte[result.count];
			readChars(data, pointer, result.charData);
			break;
		case Type.INTEGER:
			result.data = new byte[result.count];
			readInts(data, pointer, result.intData);
			break;
		case Type.LONG:
			result.data = new byte[result.count];
			readLongs(data, pointer, result.longData);
			break;
		case Type.FLOAT:
			result.data = new byte[result.count];
			readFloats(data, pointer, result.floatData);
			break;
		case Type.DOUBLE:
			result.data = new byte[result.count];
			readDoubles(data, pointer, result.doubleData);
			break;
		case Type.BOOLEAN:
			result.data = new byte[result.count];
			readBooleans(data, pointer, result.booleanData);
			break;
		}
		
		pointer += result.count * Type.getSize(result.type);
		
		return result;
	}

}
