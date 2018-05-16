package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread {
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];

	public Server() throws Exception {
		socket = new DatagramSocket(1234);
		System.out.println("Socket done");
	}

	public void run() {
		running = true;

		while (running)

		{
			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				for (int i=0; i < 5; i++)
				{
				socket.receive(packet);
				System.out.println(packet.toString());
				}
			socket.wait(100);	
			} catch (Exception e) {
			}

		}
	}
}
