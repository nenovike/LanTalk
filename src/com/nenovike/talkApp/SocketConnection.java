package com.nenovike.talkApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {
	Socket client;

	SocketConnection(Socket client) throws IOException{
		this.client = client;
		BufferedReader socketInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter socketOutput = new PrintWriter(client.getOutputStream(), true );
		new ConsoleInput(socketOutput).start();
		new ConsoleOutput(socketInput).start();
	}
	
	SocketConnection(String ip, int port) throws IOException{
		this.client = new Socket(ip, port);
		BufferedReader socketInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter socketOutput = new PrintWriter(client.getOutputStream(), true );
		new ConsoleInput(socketOutput).start();
		new ConsoleOutput(socketInput).start();
	}
}
