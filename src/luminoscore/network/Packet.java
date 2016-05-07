package luminoscore.network;

public class Packet {

	private PacketType packetType;
	private byte[] data;
	
	public Packet(PacketType packetType, byte[] data) {
		this.packetType = packetType;
		this.data = data;
	}
	
	public int getPacketID() {
		return packetType.getID();
	}
	
	public int getSize() {
		return (Integer.BYTES + data.length);
	}
	
	public byte[] getBytes() {
		return data;
	}

}
