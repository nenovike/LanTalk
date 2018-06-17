package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.nenovike.lanTalk.MainWindow;

import javafx.application.Platform;

public abstract class ListenServer implements Runnable{
	protected MainWindow mainWindow;
	protected DatagramSocket socket;
	protected byte[] buf = new byte[257];
	public boolean running;

	public ListenServer(MainWindow mainWindow, DatagramSocket socket) {
		this.socket = socket;
		this.mainWindow = mainWindow;
	}

	public void run(){
		DatagramPacket newPacket = new DatagramPacket(buf, buf.length);
		running = true;
		while (running) {
			try {
				socket.setSoTimeout(1000);
				socket.receive(newPacket);
				handlePacket(newPacket);
			} catch (Exception e) {
			}
		}
		socket.close();
	}
	
	public void terminate()
	{
		running = false;
	}

	public void handlePacket(DatagramPacket packet) throws IOException{
	}
}
