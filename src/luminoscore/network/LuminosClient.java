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
 * UDP Client of engine
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LuminosClient extends Thread {
	
	private DatagramSocket clientSocket;
	private InetAddress address;
	
	/**
	 * Contructor
	 * 
	 * @param address Client's address
	 */
	public LuminosClient(InetAddress address) {
		this.address = address;
	}
	
	/**
	 * Constructor
	 * 
	 * @param address	Client's address represented as a string
	 */
	public LuminosClient(String address) {
		try {
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the client thread
	 */
	public void run() {
		
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			Debug.addData(e.getMessage());
			Debug.print();
		}
		
		while(GlobalLock.INITIATED) {
			receiveData();
		}
		
	}

	/**
	 * Sends data
	 * 
	 * @param data		Data to be sent
	 * @param address	Where to send data
	 * @param port		Port to send to
	 */
	public void sendData(byte[] data, InetAddress address, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			clientSocket.send(packet);
		} catch (IOException e) {
			Debug.addData(e.getMessage());
		}
	}
	
	/**
	 * Retrieves data
	 * 
	 * @return byte array describing data
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
	 * Gets the client's socket
	 * 
	 * @return client's socket
	 */
	public DatagramSocket getClientSocket() {
		return clientSocket;
	}

	/**
	 * Gets the client's address
	 * 
	 * @return client's address
	 */
	public InetAddress getAddress() {
		return address;
	}

}
