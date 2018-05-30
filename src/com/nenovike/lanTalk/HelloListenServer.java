package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import com.nenovike.lanTalk.util.MessagePacket;

public class HelloListenServer implements Runnable {
	MainWindow mainWindow;
	MulticastSocket helloServerSocket;
	private byte[] buf = new byte[257];

	public HelloListenServer(MainWindow mainWindow, MulticastSocket helloServerSocket) throws Exception {
		this.helloServerSocket = helloServerSocket;
		this.mainWindow = mainWindow;
		System.out.println("Uruchomiony HelloListenServer");

	}

	public void run() {
		DatagramPacket newHelloPacket = new DatagramPacket(buf, buf.length);

		while (true) {
			try {
				helloServerSocket.receive(newHelloPacket);
				HandleHelloPacket(newHelloPacket);
			} catch (Exception e) {
			}
		}
	}
	
	public void HandleHelloPacket(DatagramPacket newHelloPacket) throws Exception {
		MessagePacket helloPacket = new MessagePacket(newHelloPacket.getData());
		System.out.println("Otrzyma³em pakiet");		
		if(helloPacket.getType() == 'H' && !helloPacket.getMessage().equals(MainWindow.name + MainWindow.myAddress.toString()))
		{
			mainWindow.mainWindowController.AddAddressToContactList(helloPacket.getMessage());
			SendHelloResponse(helloPacket.getMessage());
		}
	}
	
	public void SendHelloResponse(String message) throws Exception {
		String host = message.substring(message.indexOf("/") + 1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));
		InetSocketAddress address = new InetSocketAddress(host, port);
		String data = MainWindow.name + MainWindow.myAddress.toString();
		byte[] buf = new MessagePacket('R', data).prepareToSend();

		helloServerSocket.send(new DatagramPacket(buf, buf.length, address));
	}
}
