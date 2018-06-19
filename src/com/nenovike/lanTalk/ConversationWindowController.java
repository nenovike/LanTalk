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

/**
 * Conversation window controller
 * 
 * @author Nenovike
 * @version 1.0
 */
public class ConversationWindowController implements Initializable {

	/**
	 * Maximum length of message in {@link #messageText}.
	 */
	int maxLength = 255;
	/**
	 * Address of target user.
	 */
	InetSocketAddress address = null;
	/**
	 * Reference to {@link MainWindowController}.
	 */
	MainWindowController mainController;
	/**
	 * Conversation text.
	 */
	@FXML
	private TextArea conversationText;
	/**
	 * Message text.
	 */
	@FXML
	private TextArea messageText;
	/**
	 * "Send" button.
	 */
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

	/**
	 * Set conversation text.
	 * 
	 * @param text
	 *            text to set.
	 */
	public void setConversationText(String text) {
		conversationText.setText(text);
	}

	/**
	 * Get conversation text.
	 * 
	 * @return text contained in conversation text area
	 */
	public String getConversationText() {
		return conversationText.getText();
	}

	/**
	 * Check if controller has other user's address set (not <code>null</code>).
	 * 
	 * @return is address not <code>null</code>
	 */
	public boolean hasAddress() {
		return address != null;
	}

}
