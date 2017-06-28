package tk.luminos.network;

import static tk.luminos.network.Net.KILOBYTE;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import tk.luminos.Application;

/**
 * 
 * Networking client for engine
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Client extends Thread {
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Application application;
	
	private static long time = System.currentTimeMillis();
	
	/**
	 * Constructor 
	 * 
	 * @param application		Application to run along side
	 * @param ipAddress			IP Address of client
	 * @throws Exception		Thrown if client cannot be created
	 */
	public Client(Application application, InetAddress ipAddress) throws Exception {
		this.application = application;
		this.ipAddress = ipAddress;
		this.socket = new DatagramSocket();
	}
	
	/**
	 * Constructor 
	 * 
	 * @param application		Application to run along side
	 * @param ipAddress			IP Address of client
	 * @throws Exception		Thrown if client cannot be created
	 */
	public Client(Application application, String ipAddress) throws Exception {
		this(application, InetAddress.getByName(ipAddress));
	}
	
	/**
	 * Runs the engine
	 */
	@Override
	public void run() {
		Thread.currentThread().setName("LUMINOS_ENGINE:_NETWORK_CLIENT");
		while (true) {
			byte[] data = new byte[KILOBYTE];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (new String(packet.getData()).trim().equalsIgnoreCase("pong")) {
				try {
					time = System.currentTimeMillis();
					sendData("PING".getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (application.shouldClose)
				break;
		}
	}
	
	/**
	 * Sends data to server
	 * 
	 * @param data			Data to send
	 * @throws Exception	Thrown if packet cannot be sent
	 */
	public void sendData(byte[] data) throws Exception {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		socket.send(packet);
	}
	
	/**
	 * Gets ping of client to server
	 * 
	 * @return		Client ping in server
	 */
	public int getPing() {
		return (int) (System.currentTimeMillis() - time);
	}

}
