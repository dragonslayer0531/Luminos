package luminoscore.network;


public enum PacketType {
	
	INVALID(-01),
	CONNECT(00),
	ENTITY(01),
	TEXT(02),
	DISCONNECT(03);

	private int id = 0;
	
	PacketType(int ID) {
		this.id = ID;
	}
	
	public int getID() {
		return id;
	}
	
	public static PacketType getPacketType(int id) {
		return PacketType.values()[id + 1];
	}
	
}

