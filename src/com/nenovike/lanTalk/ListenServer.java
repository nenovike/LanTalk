package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.nenovike.lanTalk.util.MessagePacket;

public class ListenServer implements Runnable {
	MainWindow mainWindow;
	DatagramSocket serverSocket;
	private byte[] buf = new byte[257];

	public ListenServer(MainWindow mainWindow, InetSocketAddress address) throws Exception {
		this.serverSocket = new DatagramSocket(address);
		this.mainWindow = mainWindow;
		System.out.println("Uruchomiony ListenServer");
	}

	public void run() {
		DatagramPacket newHelloPacket = new DatagramPacket(buf, buf.length);

		while (true) {
			try {
				serverSocket.receive(newHelloPacket);
				HandleHelloPacket(newHelloPacket);
			} catch (Exception e) {
			}
		}
	}
	
	public void HandleHelloPacket(DatagramPacket newHelloPacket) throws Exception {
		MessagePacket helloPacket = new MessagePacket(newHelloPacket.getData());
		System.out.println("Otrzyma³em pakiet");		
		if(helloPacket.getType() == 'R')
		{
			mainWindow.mainWindowController.AddAddressToContactList(helloPacket.getMessage());
		}
	}
	
	public void SendHelloResponse(String message) throws Exception {
		String host = message.substring(1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));
		InetSocketAddress address = new InetSocketAddress(host, port);
		String helloSocketAddress = MainWindow.myAddress.toString();
		byte[] buf = new MessagePacket('R', helloSocketAddress).prepareToSend();

		serverSocket.send(new DatagramPacket(buf, buf.length, address));
		System.out.println("FAKE " + helloSocketAddress + " " + address.toString());
	}
}
