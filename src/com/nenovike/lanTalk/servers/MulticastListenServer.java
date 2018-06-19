package com.nenovike.lanTalk.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import com.nenovike.lanTalk.MainWindow;
import com.nenovike.lanTalk.util.MessagePacket;

import javafx.application.Platform;
/**
 * Mulitcast listen server class.
 * 
 * @author Nenovike
 * @version 1.0
 * 
 */
public class MulticastListenServer extends ListenServer {
	/**
	 * Multicast socket to listen on.
	 */
	MulticastSocket socket;

	/**
	 * Create new instance of listen server.
	 * 
	 * @param mainWindow
	 *            reference to {@link mainWindow} object
	 * @param socket
	 *            multicast socket to listen on
	 */
	public MulticastListenServer(MainWindow mainWindow, MulticastSocket socket) {
		super(mainWindow, socket);
		this.socket = socket;
	}

	/**
	 * Handle datagram packet. If packet type is 'H' (hello) add it to contact list.
	 */
	public void handlePacket(DatagramPacket newDatagramPacket) throws IOException {
		MessagePacket packet = new MessagePacket(newDatagramPacket.getData());
		if (packet.getType() == 'H' && isHelloPacketNotSelf(packet)) {
			Platform.runLater(() -> mainWindow.addContactFromMessage(packet.getMessage()));
			sendHelloResponse(packet.getMessage());
		}
	}

	/**
	 * Send "Hello" packet to user specified in message.
	 * 
	 * @param message
	 *            hello packet
	 * @throws IOException
	 */
	public void sendHelloResponse(String message) throws IOException {

		InetSocketAddress address = getSocketAddressFromHelloMessage(message);
		String data = mainWindow.getUserName() + mainWindow.getUserAddress();
		byte[] buf = new MessagePacket('R', data).prepareToSend();

		socket.send(new DatagramPacket(buf, buf.length, address));
	}

	/**
	 * Extract socket address from hello message.
	 * 
	 * @param message
	 *            hello message
	 * @return socket address
	 */
	private InetSocketAddress getSocketAddressFromHelloMessage(String message) {
		String host = message.substring(message.indexOf("/") + 1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));
		return new InetSocketAddress(host, port);
	}

	/**
	 * Check if hello packet has the same name and address as current user.
	 * 
	 * @param packet
	 *            packet to check
	 * @return is hello packet's name and address same as current user's
	 */
	private boolean isHelloPacketNotSelf(MessagePacket packet) {
		return !packet.getMessage().equals(mainWindow.getUserName() + mainWindow.getUserAddress());
	}
}
