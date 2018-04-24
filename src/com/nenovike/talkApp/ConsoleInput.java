package com.nenovike.talkApp;

import java.io.PrintWriter;

public class ConsoleInput extends TerminatableLoop {

	PrintWriter socketOutput;

	ConsoleInput(PrintWriter socketOutput){
		this.socketOutput = socketOutput;
	}

	@Override
	protected void loop() {
			String message = System.console().readLine();
			socketOutput.println(message);
	}
}
