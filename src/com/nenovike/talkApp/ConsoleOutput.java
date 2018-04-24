package com.nenovike.talkApp;

import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleOutput extends TerminatableLoop {

	BufferedReader socketInput;

	ConsoleOutput(BufferedReader socketInput) {
		this.socketInput = socketInput;
	}

	@Override
	protected void loop() {
		try {
			String message = socketInput.readLine();
			System.out.println(message);
		} catch (IOException e) {
		}
	}
}
