package com.luminos.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.luminos.filesystem.serialization.LDatabase;
import com.luminos.Debug;
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
	public boolean RUNNING = false;
	
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
			Debug.addData(e);
			Debug.print();
		}
		
		RUNNING = true;
		
		while(RUNNING) {
			receiveData();
		}
		
	}
	
	public void close() {
		RUNNING = false;
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
			Debug.addData(e);
		}
	}
	
	/**
	 * Retrieves data
	 * 
	 * @return Luminos Database describing data
	 */
	public LDatabase receiveData() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			clientSocket.receive(packet);
		} catch (IOException | NullPointerException e) {
			Debug.addData(e);
		}
		return LDatabase.Deserialize(data);
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