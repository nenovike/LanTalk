package com.nenovike.lanTalk.util;

import java.nio.charset.StandardCharsets;

public class MessagePacket {
	
	private char type;
	
	private String message;
	
	public MessagePacket(char type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public MessagePacket(byte[] packet)
	{
		type = (char)packet[0];
		byte messageLength = packet[1];
		message = new String(packet).substring(2, messageLength+2);
	}
	
	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] prepareToSend() {
		return (String.valueOf(type) + String.valueOf((char)message.length()) + message).getBytes(StandardCharsets.UTF_8);
	}
	
}
