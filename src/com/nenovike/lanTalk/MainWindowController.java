package com.nenovike.lanTalk;

import java.net.InetAddress;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class MainWindowController implements Initializable {
	
	
	
	@FXML
	private MenuItem menuChangeName;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
    
    @FXML
    private void handleMenuAction(ActionEvent action) throws Exception{
    	if (action.getSource() == menuChangeName)
    	{
    		MainWindow.ChangeName();
    	}
    }

}
