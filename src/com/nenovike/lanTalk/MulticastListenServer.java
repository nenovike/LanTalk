package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import com.nenovike.lanTalk.util.ListenServer;
import com.nenovike.lanTalk.util.MessagePacket;

public class MulticastListenServer extends ListenServer {
	MulticastSocket socket;

	public MulticastListenServer(MainWindow mainWindow, MulticastSocket socket){
		super(mainWindow, socket);
		this.socket = socket;
	}
	
	public void handlePacket(DatagramPacket newHelloPacket) throws IOException {
		MessagePacket helloPacket = new MessagePacket(newHelloPacket.getData());
		if(helloPacket.getType() == 'H' && !helloPacket.getMessage().equals(mainWindow.name + mainWindow.myAddress.toString()))
		{
			mainWindow.mainWindowController.addAddressToContactList(helloPacket.getMessage());
			sendHelloResponse(helloPacket.getMessage());
		}
	}
	
	public void sendHelloResponse(String message) throws IOException {
		String host = message.substring(message.indexOf("/") + 1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));
		InetSocketAddress address = new InetSocketAddress(host, port);
		String data = mainWindow.name + mainWindow.myAddress.toString();
		byte[] buf = new MessagePacket('R', data).prepareToSend();

		socket.send(new DatagramPacket(buf, buf.length, address));
	}
}
