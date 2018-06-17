package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

import com.nenovike.lanTalk.util.MessagePacket;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ConversationWindowController implements Initializable {

	int maxLength = 255;
	InetSocketAddress address = null;
	String name;
	MainWindowController mainController;

	@FXML
	private TextArea conversationText;
	@FXML
	private TextArea messageText;
	@FXML
	private Button buttonSend;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		messageText.setOnInputMethodTextChanged(e -> {
			if (messageText.getText().length() > maxLength) {
				String s = messageText.getText().substring(0, maxLength);
				messageText.setText(s);
			}

		});
		buttonSend.setOnAction(e -> {
			String data = messageText.getText();
			byte[] buf = new MessagePacket('M', data).prepareToSend();
			try {
				mainController.mainWindow.datagramSocket.send(new DatagramPacket(buf, buf.length, address));
				mainController.addSelfTextToConversation(address.toString(), data);
				messageText.setText("");
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		});
	}

	public void setConversationText(String text) {
		conversationText.setText(text);
	}

	public String getConversationText() {
		return conversationText.getText();
	}
	
	public boolean hasAddress() {
		return address != null;
	}
}
