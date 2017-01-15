package tk.luminos.filesystem.plaintext;

/**
 * Enumeration of data structure
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public enum DataStruct {
	INT(1),
	FLOAT(2),
	STRING(3),
	BOOLEAN(4),
	INVALID(0);
	
	private int id;
	
	private DataStruct(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the data structure corresponding to the ID
	 * 
	 * @param id		ID of data structure
	 * @return			Data structure of corresponding ID
	 */
	public static DataStruct getByID(int id) {
		for (DataStruct ds : DataStruct.values()) {
			if (ds.id == id) {
				return ds;
			}
		}
		return INVALID;
	}
	
	/**
	 * Gets ID of data structure
	 * 
	 * @return	ID of data structure
	 */
	public int getID() {
		return id;
	}
	
}
