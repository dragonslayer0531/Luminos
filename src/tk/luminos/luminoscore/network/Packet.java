package tk.luminos.luminoscore.network;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

/**
 * 
 * Packet of data
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Packet {

	private PacketType packetType;
	private byte[] data;
	
	/**
	 * Constructor
	 * 
	 * @param packetType	PacketType of packet
	 * @param data			Data contained in packet
	 */
	public Packet(PacketType packetType, byte[] data) {
		this.packetType = packetType;
		this.data = data;
	}
	
	/**
	 * Gets the packet's type
	 * 
	 * @return packet's type
	 */
	public int getPacketID() {
		return packetType.getID();
	}
	
	/**
	 * Gets the size of the packet
	 * 
	 * @return packet's size
	 */
	public int getSize() {
		return (Integer.BYTES + data.length);
	}
	
	/**
	 * Gets bytes of packet
	 * 
	 * @return bytes of packet
	 */
	public byte[] getBytes() {
		ByteBuffer buffer = BufferUtils.createByteBuffer(getSize());
		buffer.put(packetType.getBytes());
		buffer.put(data, Integer.BYTES, data.length);
		buffer.flip();
		return buffer.array();
	}

}
