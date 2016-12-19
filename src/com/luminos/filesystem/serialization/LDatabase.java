package com.luminos.filesystem.serialization;

import static com.luminos.filesystem.serialization.SerializationUtils.readByte;
import static com.luminos.filesystem.serialization.SerializationUtils.readInt;
import static com.luminos.filesystem.serialization.SerializationUtils.readShort;
import static com.luminos.filesystem.serialization.SerializationUtils.readString;
import static com.luminos.filesystem.serialization.SerializationUtils.writeBytes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Creates a database for serialization
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LDatabase extends LBase {

	public static final byte[] HEADER = "LDB".getBytes();
	public static final short VERSION = 0x0100;
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE;
	private short objectCount;
	public List<LObject> objects = new ArrayList<LObject>();
	
	/**
	 * Private Constructor
	 */
	private LDatabase() {
	}
	
	/**
	 * Public Constructor
	 * 
	 * @param name		Sets name of database
	 */
	public LDatabase(String name) {
		setName(name);
		
		size += HEADER.length + 2 + 1 + 2;
	}
	
	/**
	 * Adds {@link LObject} to database
	 * 
	 * @param object	Luminos Object to add
	 */
	public void addObject(LObject object) {
		objects.add(object);
		size += object.getSize();
		
		objectCount = (short)objects.size();
	}
	
	/**
	 * Gets the size of the Luminos Database
	 * 
	 * @return	Size of the Luminos Database
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the byte array describing the Luminos Database
	 * 
	 * @param dest		Destination byte array to write to
	 * @param pointer	Pointer to start the write at
	 * @return			Final pointer location
	 */
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, HEADER);
		pointer = writeBytes(dest, pointer, VERSION);
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, objectCount);
		for (LObject object : objects)
			pointer = object.getBytes(dest, pointer);
		
		return pointer;
	}
	
	/**
	 * Deserializes raw bytes into a Luminos Database
	 * 
	 * @param data		Raw data to deserialize from
	 * @return			Luminos Database that was deserialized to
	 */
	public static LDatabase Deserialize(byte[] data) {
		int pointer = 0;
		assert(readString(data, pointer, HEADER.length).equals(HEADER));
		pointer += HEADER.length;
		
		if (readShort(data, pointer) != VERSION) {
			return null;
		}
		pointer += 2;
		
		byte containerType = readByte(data, pointer++);
		assert(containerType == CONTAINER_TYPE);
		
		LDatabase result = new LDatabase();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.objectCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.objectCount; i++) {
			LObject object = LObject.Deserialize(data, pointer);
			result.objects.add(object);
			pointer += object.getSize(); 
		}
		
		return result;
	}
	
	/**
	 * Finds Luminos Object inside Luminos Object Database
	 * 
	 * @param name		Name of object to find
	 * @return			Luminos Object with the given name
	 */
	public LObject findObject(String name) {
		for (LObject object : objects) {
			if (object.getName().equalsIgnoreCase(name));
				return object;
		}
		return null;
	}

	/**
	 * Loads a Luminos Database from file
	 * 
	 * @param path	File location
	 * @return		Luminos Database to load to
	 */
	public static LDatabase DeserializeFromFile(String path) {
		byte[] buffer = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Deserialize(buffer);
	}
	
	/**
	 * Prints the Luminos Database to file
	 * 
	 * @param path		File to print to
	 */
	public void serializeToFile(String path) {
		byte[] data = new byte[getSize()];
		getBytes(data, 0);
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
			stream.write(data);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

