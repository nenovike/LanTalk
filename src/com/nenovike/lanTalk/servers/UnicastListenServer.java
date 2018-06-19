package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.nenovike.lanTalk.MainWindow;
import com.nenovike.lanTalk.util.MessagePacket;

import javafx.application.Platform;

/**
 * Unicast listen server class.
 * 
 * @author Nenovike
 * @version 1.0
 * 
 */
public class UnicastListenServer extends ListenServer {
	/**
	 * New instance of Listen server.
	 * 
	 * @param mainWindow
	 *            reference to mainWindow
	 * @param socket
	 *            unicast socket to listen on
	 */
	public UnicastListenServer(MainWindow mainWindow, DatagramSocket socket) {
		super(mainWindow, socket);
	}

	/**
	 * Handle datagram packet. If packet type is 'R'(hello response) add it's
	 * address to contact list. If packet type is 'M'(message), write the message to
	 * correct conversation.
	 */
	public void handlePacket(DatagramPacket newPacket) throws IOException {
		MessagePacket packet = new MessagePacket(newPacket.getData());
		if (packet.getType() == 'R') {
			Platform.runLater(() -> mainWindow.addContactFromMessage(packet.getMessage()));
		}
		if (packet.getType() == 'M') {
			InetSocketAddress address = (InetSocketAddress) newPacket.getSocketAddress();
			Platform.runLater(() -> mainWindow.getMainWindowController().addOthersTextToConversation(address.toString(),
					packet.getMessage()));
		}
	}
}
