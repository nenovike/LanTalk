package com.nenovike.talkApp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server extends TerminatableLoop {

	ServerSocket server;

	Server(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Created socket on " + InetAddress.getLocalHost().getHostAddress() + ":"
					+ server.getLocalPort() + ".");
		} catch (IOException e) {

		}
	}

	@Override
	protected void loop() {
		try {
			System.out.println("Listening");
			new SocketConnection(server.accept()).start();
		} catch (IOException e) {
			
		}
	}
}
