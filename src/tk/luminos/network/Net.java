package tk.luminos.network;

/**
 * Networking utilities
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Net {
	
	protected static final int BYTE = 1;
	protected static final int KILOBYTE = BYTE * 1024;
	protected static final int MEGABYTE = KILOBYTE * 1024;
	protected static final int GIGABYTE = MEGABYTE * 1024;
	
	public enum PacketType {
		   INVALID(0x0),
			 LOGIN(0x1),
		DISCONNECT(0x2);
		
		private int id;
		private PacketType(int id) {
			this.id = id;
		}
		
		/**
		 * Gets ID of packet
		 * 
		 * @return Packet type
		 */
		public int getID() {
			return id;
		}
		
		/**
		 * Gets packet by ID
		 * @param id		Packet ID
		 * @return			PacketType given by ID
		 */
		public static PacketType getPacketByID(int id) {
			for (PacketType packet : PacketType.values()) 
				if (packet.id == id)
					return packet;
			return INVALID;
		}
		
	}

}
