package com.luminos.network;

public class Net {
	
	public static final int BYTE = 1;
	public static final int KILOBYTE = BYTE * 1024;
	public static final int MEGABYTE = KILOBYTE * 1024;
	public static final int GIGABYTE = MEGABYTE * 1024;
	
	public enum PacketType {
		   INVALID(0x0),
			 LOGIN(0x1),
		DISCONNECT(0x2);
		
		private int id;
		PacketType(int id) {
			this.id = id;
		}
		
		public int getID() {
			return id;
		}
		
		public static PacketType getPacketByID(int id) {
			for (PacketType packet : PacketType.values()) 
				if (packet.id == id)
					return packet;
			return INVALID;
		}
		
		public static int getPacketSize(PacketType packet) {
			switch (packet) {
			case LOGIN:
				return 8 * BYTE;
			case DISCONNECT:
				return 8 * BYTE;
			default:
				return BYTE;
			}
		}
	}

}
