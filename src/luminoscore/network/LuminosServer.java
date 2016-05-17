package luminoscore.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import luminoscore.Debug;
import luminosutils.serialization.SerializationUtils;

public class LuminosServer extends Thread {

	private static int port;
	private static boolean running = true;
	private static InetAddress address;
	private static DatagramSocket serverSocket;
	
	private static List<InetAddress> clients = new ArrayList<InetAddress>();
	
	public LuminosServer(int port) {
		LuminosServer.port = port;
	}
	
	public void run() {
		
		try {
			serverSocket = new DatagramSocket();
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			Debug.addData(e.getMessage());
			Debug.print();
		} catch (SocketException e) {
			Debug.addData(e.getMessage());
			Debug.print();
		}
		
		while(running) {
			/*
			 * SERVER RUNNING
			 * PLAYER CONNECTIONS/DISCONNECTIONS
			 * ENTITY PLACEMENT/DESTRUCTION
			 * TEXT HANDLING
			 * 
			 * if(Client Connects) {
			 * 		Add client to list
			 * }
			 * 
			 * if(Receives packet) {
			 * 		Read Packet
			 * 		Handle Packet Data
			 * }
			 */
		}
		
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		LuminosServer.running = running;
	}

	public static int getPort() {
		return port;
	}

	public static InetAddress getAddress() {
		return address;
	}
	
	private Packet readPacket(DatagramPacket packet) {
		byte[] raw = packet.getData();
		byte[] type = new byte[] {raw[0], raw[1]};
		byte[] data = new byte[raw.length - type.length];
		for(int pointer = type.length; pointer < data.length; pointer++) data[pointer] = raw[pointer + data.length];
		return new Packet(PacketType.getPacketType(SerializationUtils.readInt(type, 0)), data);
	} 
	
	private void sendData(InetAddress client, int port, Packet packet) {
		DatagramPacket data = new DatagramPacket(packet.getBytes(), packet.getSize(), client, port);
		try {
			serverSocket.send(data);
		} catch (IOException e) {
			Debug.addData(e.getMessage());
		}
	}
	
	private void sendDataToAll(int port, Packet packet) {
		for(InetAddress address : clients) {
			sendData(address, port, packet);
		}
	}

}
