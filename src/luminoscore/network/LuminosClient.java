package luminoscore.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import luminoscore.Debug;
import luminoscore.GlobalLock;

public class LuminosClient extends Thread {
	
	private static int port;
	private static DatagramSocket clientSocket;
	
	public LuminosClient(int port) {
		LuminosClient.port = port;
	}

	public void run() {
		
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			Debug.addData(e.getMessage());
			Debug.print();
		}
		
		while(GlobalLock.INITIATED) {
			/*
			 * SEND CLIENT DATA
			 */
		}
		
	}
	
	public void sendData() {
		
	}
	
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

}
