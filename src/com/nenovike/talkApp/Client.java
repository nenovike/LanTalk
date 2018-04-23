package com.nenovike.talkApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends TerminatableLoop {

	Socket server;
	BufferedReader socketInput;
	PrintWriter socketOutput;

	Client(String ip, int port) {
		try {
			server = new Socket(ip, port);
			System.out.println("Established connection to " + ip + ":" + port + ".");
		} catch (UnknownHostException e) {
			System.out.println("Can't find host.");
		} catch (IOException e) {
			System.out.println("Error connecting to host.");
		}
	}

	@Override
	protected void prepare() {
		try {
			socketInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
			socketOutput = new PrintWriter(server.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("I/O error " + e);
		}
	}

	@Override
	protected void loop() {
		try {
			System.out.print("New message: ");
			String message = System.console().readLine();
			socketOutput.println(message);
			System.out.println(socketInput.readLine());
		} catch (IOException e) {

		}
	}
}
