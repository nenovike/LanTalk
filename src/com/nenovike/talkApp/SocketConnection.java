package com.nenovike.talkApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class SocketConnection extends TerminatableLoop {
	Socket client;
	BufferedReader socketInput;
	PrintWriter socketOutput;

	public void terminate() {
		super.terminate();
		try {
			client.close();
		} catch (IOException E) {

		}
	}

	SocketConnection(Socket client) {
		System.out.println("New Connection");
		this.client = client;
	}

	@Override
	protected void loop() {
		try {
			System.out.println("Waiting for message.");
			BufferedReader socketInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter socketOutput = new PrintWriter(client.getOutputStream(), true );
			String message = socketInput.readLine();
			System.out.println("R:" + message);
			socketOutput.println("OK");			
		} catch (SocketException e) {
			System.out.println("Socket error " + e);
			if(e.getMessage().equalsIgnoreCase("Connection reset"))
				running = false;
		} catch (IOException e) {
			System.out.println("I/O error " + e);
		} 
	}
}
