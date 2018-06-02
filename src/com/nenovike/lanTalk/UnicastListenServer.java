package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.nenovike.lanTalk.util.ListenServer;
import com.nenovike.lanTalk.util.MessagePacket;

public class UnicastListenServer extends ListenServer {

	public UnicastListenServer(MainWindow mainWindow, DatagramSocket socket) {
		super(mainWindow, socket);
	}
	
	public void handlePacket(DatagramPacket packet) throws IOException {
		MessagePacket newPacket = new MessagePacket(packet.getData());
		if(newPacket.getType() == 'R')
		{
			mainWindow.mainWindowController.addAddressToContactList(newPacket.getMessage());
		}
	}
}
