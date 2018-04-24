package com.nenovike.talkApp;

import java.io.IOException;

public class TalkApp {

	public static void main(String[] args) throws IOException {
		System.out.println("Running");
		Server talkServer = new Server(Integer.parseInt(args[0]));
		talkServer.start();
		if (System.console().readLine().equalsIgnoreCase("y")) {
			String socket = System.console().readLine();
			String ip = socket.substring(0, socket.indexOf(':'));
			int port = Integer.parseInt(socket.substring(socket.indexOf(':') + 1));
			new SocketConnection(ip, port);
		}
	}
}