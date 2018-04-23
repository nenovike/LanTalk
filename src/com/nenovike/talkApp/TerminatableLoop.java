package com.nenovike.talkApp;

public abstract class TerminatableLoop extends Thread {
	
	protected volatile boolean running = true;

	public void terminate() {
		running = false;
	}

	public void run() {
		prepare();

		while (running) {
			loop();
		}

	}

	protected void prepare() {

	}

	protected void loop() {

	}
}
