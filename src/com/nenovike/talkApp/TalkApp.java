package com.nenovike.talkApp;

import java.io.IOException;

public class TalkApp {

	public static void main(String[] args) throws IOException {
		System.out.println("Running");
		Server talkServer;
		Client talkClient;
		if (args[0].equalsIgnoreCase("s")) {
			talkServer = new Server(Integer.parseInt(args[1]));
			talkServer.start();
		}
		else if (args[0].equalsIgnoreCase("c")) {
			String ip = args[1].substring(0, args[1].indexOf(':'));
			int port = Integer.parseInt(args[1].substring(args[1].indexOf(':') + 1));
			talkClient = new Client(ip, port);
			talkClient.start();
		}
		else
		{
			System.out.println("STH WRUNG");
		}
	}

}