package com.nenovike.lanTalk;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class ConversationWindowController implements Initializable {

	int maxLength = 255;
	
	@FXML
	private TextField conversationText;
	@FXML
	private TextField messageText;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		messageText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (messageText.getText().length() > maxLength) {
	                String s = messageText.getText().substring(0, maxLength);
	                messageText.setText(s);
	            }
	        }
	    });
		
	}

	public void setConversationText(String text) {
		conversationText.setText(text);
	}
	
	public String getConversationText() {
		return conversationText.getText();
	}
	
	
}
