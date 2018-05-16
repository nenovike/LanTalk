package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {
	MulticastSocket serverSocket = new MulticastSocket(1111);
	private boolean running;
	private byte[] buf = new byte[256];

	public Server() throws Exception {
	}

	public void run() {
		try {
			serverSocket.joinGroup(InetAddress.getByName("233.0.0.0"));
		} catch (Exception e) {
		}
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while (true) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
				System.out.println("Listening...");
				serverSocket.receive(receivePacket);
				System.out.println(new String(packet.getData(), StandardCharsets.UTF_8));
				
			} catch (Exception e) {
			}

		}
	}
}
