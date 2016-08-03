package tk.luminos.filesystem.serialization;

/**
 * 
 * Base abstract class for all other Luminos Serialization Objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class LBase {

	protected short nameLength;
	protected byte[] name;

	protected int size = 2 + 4;
	
	/**
	 * Gets the name of the Luminos Serialization Object
	 * 
	 * @return	name of the Luminos Serialization Object
	 */
	public String getName() {
		return new String(name, 0, nameLength);
	}
	
	/**
	 * Sets the name of the Luminos Serialization Object
	 * 
	 * @param name		String containing name of the Luminos Serialization Object
	 */
	public void setName(String name) {
		assert(name.length() < Short.MAX_VALUE);
		
		if (this.name != null)
			size -= this.name.length;
		
		nameLength = (short)name.length();
		this.name = name.getBytes();
		size += nameLength;
	}
	
	/**
	 * Gets the size of the Luminos Serialization Object
	 * 
	 * @return	size of the Luminos Serialization Object
	 */
	public abstract int getSize();
	
}