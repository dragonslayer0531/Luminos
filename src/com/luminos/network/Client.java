package com.luminos.network;

import static com.luminos.network.Net.KILOBYTE;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.luminos.Application;

public class Client extends Thread {
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Application application;
	
	private static long time = System.currentTimeMillis();
	
	public Client(Application application, InetAddress ipAddress) throws Exception {
		this.application = application;
		this.ipAddress = ipAddress;
		this.socket = new DatagramSocket();
	}
	
	public Client(Application application, String ipAddress) throws Exception {
		this(application, InetAddress.getByName(ipAddress));
	}
	
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
			if (application.application_Close)
				break;
		}
	}
	
	public void sendData(byte[] data) throws Exception {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		socket.send(packet);
	}
	
	public int getPing() {
		return (int) (System.currentTimeMillis() - time);
	}

}
