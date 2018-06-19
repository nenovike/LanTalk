package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.nenovike.lanTalk.MainWindow;

/**
 * Listen server abstract class.
 * 
 * @author Nenovike
 * @version 1.0
 */
public abstract class ListenServer implements Runnable {
	/**
	 * Reference to {@link MainWindow} class.
	 */
	protected MainWindow mainWindow;
	/**
	 * Default socket reference.
	 */
	protected DatagramSocket socket;
	/**
	 * Message buffer.
	 */
	protected byte[] buf = new byte[257];
	/**
	 * Running flag. If false, stop server.
	 */
	public boolean running;

	/**
	 * Create new instance of listen server.
	 * 
	 * @param mainWindow
	 *            reference to {@link mainWindow} object
	 * @param socket
	 *            socket to listen on
	 */
	public ListenServer(MainWindow mainWindow, DatagramSocket socket) {
		this.socket = socket;
		this.mainWindow = mainWindow;
	}

	public void run() {
		DatagramPacket newPacket = new DatagramPacket(buf, buf.length);
		running = true;
		while (running) {
			try {
				socket.setSoTimeout(5000);
				socket.receive(newPacket);
				handlePacket(newPacket);
			} catch (Exception e) {
			}
		}
		socket.close();
	}

	/**
	 * Terminate server thread.
	 */
	public void terminate() {
		running = false;
	}

	/**
	 * Handle packet default method.
	 * 
	 * @param packet
	 *            datagram packet to handle
	 * @throws IOException
	 */
	public void handlePacket(DatagramPacket packet) throws IOException {
	}
}
