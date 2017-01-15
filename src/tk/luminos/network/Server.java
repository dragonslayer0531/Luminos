package tk.luminos.network;

import static tk.luminos.network.Net.KILOBYTE;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import tk.luminos.Application;

public class Server extends Thread {
	
	private DatagramSocket socket;
	private Application application;
	
	public Server(Application application) throws Exception {
		this.socket = new DatagramSocket(1331);
		this.application = application;
	}
	
	public void run() {
		Thread.currentThread().setName("LUMINOS_ENGINE:_NETWORK_SERVER");
		while (true) {
			byte[] data = new byte[KILOBYTE];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (new String(packet.getData()).trim().equalsIgnoreCase("PING")) {
				try {
					sendData("PONG".getBytes(), packet.getAddress(), packet.getPort());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (application.application_Close)
				break;
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) throws Exception {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		socket.send(packet);
	}

}