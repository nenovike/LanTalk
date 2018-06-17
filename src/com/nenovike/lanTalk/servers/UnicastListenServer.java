package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
	}
}
