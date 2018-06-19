package com.nenovike.lanTalk.util;

import java.net.InetSocketAddress;

import javafx.scene.control.Button;

/**
 * Contact button class. Extends {@link Button}.
 * 
 * @author Nenovike
 * @version 1.0
 */
public class ContactButton extends Button {
	/**
	 * Name of user the button is connected to.
	 */
	private String name;
	/**
	 * Address of user the button is connected to.
	 */
	private InetSocketAddress address;

	/**
	 * New instance of button.
	 * 
	 * @param name
	 *            name of user the button is connected to
	 * @param address
	 *            address of user the button is connected to
	 */
	public ContactButton(String name, InetSocketAddress address) {
		this.name = name;
		this.address = address;
	}

	/**
	 * New instance using message.
	 * 
	 * @param message
	 *            packet message to extract name and address from
	 */
	public ContactButton(String message) {
		String name = message.substring(0, message.indexOf("/"));
		String address = message.substring(message.indexOf("/") + 1, message.indexOf(":"));
		int port = Integer.parseInt(message.substring(message.indexOf(":") + 1));

		this.name = name;
		this.address = new InetSocketAddress(address, port);

		setText(message);
	}

	/**
	 * Get user name.
	 * 
	 * @return user name connected to button
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set user name and update buttons text.
	 * 
	 * @param name
	 *            user name to set
	 */
	public void setName(String name) {
		this.name = name;
		setText(name + address.toString());
	}
	/**
	 * Get user address.
	 * 
	 * @return user address connected to button
	 */
	public InetSocketAddress getAddress() {
		return address;
	}
}
