package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.nenovike.lanTalk.util.MessagePacket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

public class MainWindowController implements Initializable {

	public MainWindow mainWindow = null;

	@FXML
	private MenuItem menuChangeName;
	public TextArea contactList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	private void handleMenuAction(ActionEvent action) throws IOException  {
		if (action.getSource() == menuChangeName) {
			changeName();
		}
	}

	public void changeName() throws IOException  {
		TextInputDialog nameDialog = new TextInputDialog("LanTalk");
		nameDialog.setTitle("LanTalk");
		nameDialog.setHeaderText("What is your name?");
		nameDialog.setContentText("Enter it here: ");
		Optional<String> result;
		do {
			result = nameDialog.showAndWait();
		} while (!result.isPresent() || result.get().length() > 10);
		mainWindow.name = result.get();
		mainWindow.mainWindowStage.setTitle(mainWindow.name + mainWindow.myAddress);
		resyncAddresses();
	}

	public void resyncAddresses() throws IOException {
		contactList.setText("");
		sendMulticastHelloPacket(mainWindow.defaultHelloAddress);
	}

	private void sendMulticastHelloPacket(InetSocketAddress defaultHelloAddress) throws IOException {

		String data = mainWindow.name + mainWindow.myAddress.toString();
		byte[] buf = new MessagePacket('H', data).prepareToSend();

		mainWindow.multicastSocket.send(new DatagramPacket(buf, buf.length, defaultHelloAddress));
	}

	public void addAddressToContactList(String message) {
		String address = message.substring(message.indexOf("/"));
		String name = message.substring(0, message.indexOf("/"));
		if (contactList.getText().contains(address)) {
			String text = contactList.getText();
			int pos = text.indexOf(address);
			if (pos < 12)
				text = text.replaceFirst(text.substring(0, pos), name);
			else
				text = text.replaceFirst(text.substring(text.indexOf("\n", pos - 12), text.indexOf("/", pos)), name);
			contactList.setText(text);
		} else
			contactList.setText(contactList.getText() + message + "\n");
	}

}
