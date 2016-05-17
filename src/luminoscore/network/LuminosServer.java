package luminoscore.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import luminoscore.Debug;
import luminoscore.GlobalLock;

/**
 * 
 * UDP Server of engine
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LuminosServer extends Thread {

	private int port = 1331;
	private DatagramSocket clientSocket;
	private InetAddress serverAddress;
	
	/**
	 * Constructor
	 * 
	 * @param address	Address of server
	 */
	public LuminosServer(InetAddress address) {
		this.serverAddress = address;
	}
	
	/**
	 * Constructor
	 * 
	 * @param address	Address of server
	 * @param port		Port of server
	 */
	public LuminosServer(InetAddress address, int port) {
		this.serverAddress = address;
		this.port = port;
	}
	
	/**
	 * Constructor
	 * 
	 * @param address	Address of server
	 */
	public LuminosServer(String address) {
		try {
			this.serverAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param address	Address of server
	 * @param port		Port of server
	 */
	public LuminosServer(String address, int port) {
		try {
			this.serverAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}

	/**
	 * Starts thread
	 */
	public void run() {
		
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			Debug.addData(e.getMessage());
			Debug.print();
		}
		
		while(GlobalLock.INITIATED) {
			receiveData();
		}
		
	}

	/**
	 * Sends the data
	 * 
	 * @param data to send
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			clientSocket.send(packet);
		} catch (IOException e) {
			Debug.addData(e.getMessage());
		}
	}
	
	/**
	 * Receives data
	 * 
	 * @return	data received
	 */
	public byte[] receiveData() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			clientSocket.receive(packet);
		} catch (IOException e) {
			Debug.addData(e.getMessage());
			return new byte[1024];
		}
		return data;
	}
	
	/**
	 * Gets the Server's DatagramSocket
	 * 
	 * @return	Server's DatagramSocket
	 */
	public DatagramSocket getServerSocket() {
		return clientSocket;
	}

	/**
	 * Gets the Server's Address
	 * 
	 * @return 	Server's Address
	 */
	public InetAddress getServerAddress() {
		return serverAddress;
	}

}
