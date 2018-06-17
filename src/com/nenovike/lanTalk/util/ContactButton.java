package com.nenovike.lanTalk.util;

import java.net.InetSocketAddress;

import javafx.scene.control.Button;

public class ContactButton extends Button {

	String name;
	InetSocketAddress address;
	
	public ContactButton(String name, InetSocketAddress address) {
		this.name = name;
		this.address = address;
	}
	
	public ContactButton(String message)
	{
		String name = message.substring(0, message.indexOf("/"));
		String address = message.substring(message.indexOf("/") + 1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));
		
		this.name = name;
		this.address = new InetSocketAddress(address, port);
		
		setText(message);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

}
