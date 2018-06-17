package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import com.nenovike.lanTalk.MainWindow;
import com.nenovike.lanTalk.util.MessagePacket;

import javafx.application.Platform;

public class MulticastListenServer extends ListenServer {
	MulticastSocket socket;

	public MulticastListenServer(MainWindow mainWindow, MulticastSocket socket){
		super(mainWindow, socket);
		this.socket = socket;
	}
	
	public void handlePacket(DatagramPacket newDatagramPacket) throws IOException {
		MessagePacket packet = new MessagePacket(newDatagramPacket.getData());
		if(packet.getType() == 'H' && isHelloPacketSelf(packet))
		{
			Platform.runLater(() -> mainWindow.addContactFromMessage(packet.getMessage()));
			sendHelloResponse(packet.getMessage());
		}
	}
	
	public void sendHelloResponse(String message) throws IOException {
		
		InetSocketAddress address = getSocketAddressFromHelloMessage(message);
		String data = mainWindow.getUserName() + mainWindow.getUserAddress();
		byte[] buf = new MessagePacket('R', data).prepareToSend();

		socket.send(new DatagramPacket(buf, buf.length, address));
	}
	
	private InetSocketAddress getSocketAddressFromHelloMessage(String message)
	{
		String host = message.substring(message.indexOf("/") + 1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));
		return new InetSocketAddress(host, port);
	}
	
	private boolean isHelloPacketSelf(MessagePacket packet)
	{
		return !packet.getMessage().equals(mainWindow.getUserName() + mainWindow.getUserAddress());
	}
}
