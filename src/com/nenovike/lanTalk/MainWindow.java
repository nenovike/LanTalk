package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class MainWindow extends Application {
	static String name = "LanTalk";
	static Stage primaryStage;
	
	static MainWindowController mainWindowController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("MainWindow.fxml"));
		mainWindowController = (MainWindowController) fxmlLoader.getController();
		Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        ChangeName();
	}
	
	public static void ChangeName() throws Exception{
		TextInputDialog nameDialog = new TextInputDialog("LanTalk");
        nameDialog.setTitle("LanTalk");
        nameDialog.setHeaderText("What is your name?");
        nameDialog.setContentText("Enter it here: ");
        Optional<String> result;
        do {
        	result = nameDialog.showAndWait();
        }
        while(result.isPresent()&&result.get().length() > 10);
        if (result.isPresent()) 
            name = result.get();
        
        primaryStage.setTitle(name + "(" + InetAddress.getLocalHost().getHostAddress() + ")");
       
        new Server().run();

        
        for (InterfaceAddress na : NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses()) 
        {
            InetAddress broadcast = na.getBroadcast();
            if (broadcast == null)
                continue;
            System.out.println("Sent to " + broadcast);

            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] buffer = "Siema".getBytes();
            
            socket.send(new DatagramPacket(buffer, buffer.length, broadcast, 1234));
            System.out.println("Sent to " + broadcast);
            socket.close();
        }

	}

	public static void main(String[] args) {
		MainWindow.launch(args);
	}

}
