package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.nenovike.lanTalk.MainWindow;
import com.nenovike.lanTalk.util.MessagePacket;

import javafx.application.Platform;

public class UnicastListenServer extends ListenServer {

	public UnicastListenServer(MainWindow mainWindow, DatagramSocket socket) {
		super(mainWindow, socket);
	}
	
	public void handlePacket(DatagramPacket newPacket) throws IOException {
		MessagePacket packet = new MessagePacket(newPacket.getData());
		if(packet.getType() == 'R')
		{
			Platform.runLater(() -> mainWindow.addContactFromMessage(packet.getMessage()));
		}
		if(packet.getType() == 'M')
		{
			InetSocketAddress address = (InetSocketAddress) newPacket.getSocketAddress();
			Platform.runLater(() -> mainWindow.mainWindowController.addOthersTextToConversation(address.toString(), packet.getMessage()));
		}
	}
}
