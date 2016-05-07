package luminosutils.serialization;

import static luminosutils.serialization.SerializationUtils.readInt;
import static luminosutils.serialization.SerializationUtils.readShort;
import static luminosutils.serialization.SerializationUtils.readString;
import static luminosutils.serialization.SerializationUtils.writeBytes;

import java.util.ArrayList;
import java.util.List;

public class LObject extends Base {
	
	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	private short fieldCount;
	public List<LField> fields = new ArrayList<LField>();
	private short stringCount;
	public List<LString> strings = new ArrayList<LString>();
	private short arrayCount;
	public List<LArray> arrays = new ArrayList<LArray>();
	
	public LObject() {
		
	}
	
	public LObject(String name) {
		size += 1 + 2 + 2 + 2;
		setName(name);
	}
	
	public void addField(LField field) {
		fields.add(field);
		size += field.getSize();
		fieldCount = (short) fields.size();
	}
	
	public void addString(LString string) {
		strings.add(string);
		size += string.getSize();
		stringCount = (short) strings.size();
	}
	
	public void addArray(LArray array) {
		arrays.add(array);
		size += array.getSize();
		arrayCount = (short) arrays.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public LField findField(String name) {
		for(LField field : fields) {
			if(field.getName().equals(field)) return field;
		}
		return null;
	}
	
	public LString findString(String name) {
		for(LString string : strings) {
			if(string.getName().equals(name)) return string;
		}
		return null;
	}
	
	public LArray findArray(String name) {
		for(LArray array : arrays) {
			if(array.getName().equals(name)) return array;
		}
		return null;
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for(LField field : fields) {
			pointer = field.getBytes(dest, pointer);
		}
		pointer = writeBytes(dest, pointer, stringCount);
		for(LString string : strings) {
			pointer = string.getBytes(dest, pointer);
		}
		pointer = writeBytes(dest, pointer, arrayCount);
		for(LArray array : arrays) {
			pointer = array.getBytes(dest, pointer);
		}
		
		return pointer;
	}
	
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
