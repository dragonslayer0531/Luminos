package luminoscore.network;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Holds the type of the packet
 *
 */

public enum PacketType {
	
	INVALID(-01),
	CONNECT(00),
	ENTITY(01),
	TEXT(02),
	DISCONNECT(03);

	private int id = 0;
	
	/**
	 * Constructor
	 * 
	 * @param ID	
	 */
	PacketType(int ID) {
		this.id = ID;
	}
	
	/**
	 * Gets the ID of the packet type
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Gets the bytes of the packet type
	 * 
	 * @return array of bytes describing the packet type
	 */
	public byte[] getBytes() {
		byte[] bytes = new byte[Integer.BYTES];
		return bytes;
	}
	
	/**
	 * Gets packet type by ID
	 * 
	 * @param id	ID to search for
	 * @return		Packet type of ID
	 */
	public static PacketType getPacketType(int id) {
		return PacketType.values()[id + 1];
	}
	
}

