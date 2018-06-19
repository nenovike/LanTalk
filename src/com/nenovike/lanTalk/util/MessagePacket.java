package com.nenovike.lanTalk.util;

import java.nio.charset.StandardCharsets;

import javafx.scene.control.Button;

/**
 * Message packet class.
 * 
 * @author Nenovike
 * @version 1.0
 */
public class MessagePacket {
	/**
	 * Type of Message. <br>
	 * 'H' - hello <br>
	 * 'R' - hello response <br>
	 * 'M' - message
	 */
	private char type;
	/**
	 * Message text.
	 */
	private String message;

	/**
	 * New instance from type and message
	 * 
	 * @param type
	 *            {@link #type} of message
	 * @param message
	 *            message text
	 */
	public MessagePacket(char type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * New instance from message packet.
	 * 
	 * @param packet
	 *            byte array extracted from datagram packet
	 */
	public MessagePacket(byte[] packet) {
		type = (char) packet[0];
		byte messageLength = packet[1];
		message = new String(packet).substring(2, messageLength + 2);
	}

	/**
	 * Get message type
	 * 
	 * @return message type
	 */
	public char getType() {
		return type;
	}

	/**
	 * Get message text
	 * 
	 * @return message text
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Convert message type and text to byte array to send through socket.
	 * 
	 * @return byte array containing type, length and text of message
	 */
	public byte[] prepareToSend() {
		return (String.valueOf(type) + String.valueOf((char) message.length()) + message)
				.getBytes(StandardCharsets.UTF_8);
	}

}
