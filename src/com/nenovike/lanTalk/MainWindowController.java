package com.nenovike.lanTalk;

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
    private void handleMenuAction(ActionEvent action) throws Exception{
    	if (action.getSource() == menuChangeName)
    	{
    		ChangeName();
    	}
    }
    
    public void ChangeName() throws Exception {
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
		ResyncAddresses();
	}
    
    public void ResyncAddresses() throws Exception{
		contactList.setText("");

		SendMulticastHelloPacket(mainWindow.defaultHelloAddress);
	}
	
	private void SendMulticastHelloPacket(InetSocketAddress defaultHelloAddress) throws Exception {

		String data = mainWindow.name + mainWindow.myAddress.toString();
		byte[] buf = new MessagePacket('H', data).prepareToSend();

		mainWindow.helloSocket.send(new DatagramPacket(buf, buf.length, defaultHelloAddress));
	}
	
	public void AddAddressToContactList(String message) {
		String address = message.substring(message.indexOf("/"));
		String name = message.substring(2, message.indexOf("/"));
		if(contactList.getText().contains(address)) {
			String text = contactList.getText();
			int pos = text.indexOf(address);
			if (pos < 12)
				text.replace(text.substring(0, pos), name);
			else 
				text.replace(text.substring(text.indexOf("\n", pos - 12), text.indexOf("/", pos)), name);
			contactList.setText(text);
		}
		else
		contactList.setText(contactList.getText() + "\n" + message);
	}	

}
