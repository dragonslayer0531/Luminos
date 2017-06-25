package tk.luminos.serialization;

import static tk.luminos.serialization.SerializationUtils.readByte;
import static tk.luminos.serialization.SerializationUtils.readInt;
import static tk.luminos.serialization.SerializationUtils.readShort;
import static tk.luminos.serialization.SerializationUtils.readString;
import static tk.luminos.serialization.SerializationUtils.writeBytes;

import java.util.ArrayList;
import java.util.List;

public class DBObject extends DBBase {

	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	private short fieldCount;
	public List<DBField> fields = new ArrayList<DBField>();
	private short stringCount;
	public List<DBString> strings = new ArrayList<DBString>();
	private short arrayCount;
	public List<DBArray> arrays = new ArrayList<DBArray>();
	public byte objectType;
	
	private DBObject() {
		
	}
	
	public DBObject(String name, byte objectType) {
		size += 1 + 2 + 2 + 2 + 1;
		this.objectType = objectType;
		setName(name);
	}
	
	public void addField(DBField field) {
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short)fields.size();
	}
	
	public void addString(DBString string) {
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short)strings.size();
	}

	public void addArray(DBArray array) {
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short)arrays.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public DBField findField(String name) {
		for (DBField field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	public DBString findString(String name) {
		for (DBString string : strings) {
			if (string.getName().equals(name))
				return string;
		}
		return null;
	}

	public DBArray findArray(String name) {
		for (DBArray array : arrays) {
			if (array.getName().equals(name))
				return array;
		}
		return null;
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, objectType);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for (DBField field : fields)
			pointer = field.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, stringCount);
		for (DBString string : strings)
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (DBArray array : arrays)
			pointer = array.getBytes(dest, pointer);
		
		return pointer;
	}
	
	public static DBObject deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		DBObject result = new DBObject();
		result.objectType = readByte(data, pointer++);
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
				
		result.fieldCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.fieldCount; i++) {
			DBField field = DBField.deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.stringCount; i++) {
			DBString string = DBString.deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}

		result.arrayCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.arrayCount; i++) {
			DBArray array = DBArray.deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
	
}
