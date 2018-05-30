package com.nenovike.lanTalk;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MainWindow extends Application {
	
	static String name = "LanTalk";
	Stage mainWindowStage;
	MainWindowController mainWindowController;
	static InetSocketAddress defaultHelloAddress;
	static InetSocketAddress myAddress;
	MulticastSocket helloSocket;
	
	private TextArea contactList;

	@Override
	public void start(Stage mainWindowStage) throws Exception {
		defaultHelloAddress = new InetSocketAddress("233.0.0.0", 1111);
		myAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 1232);
		helloSocket = new MulticastSocket(1111);
		helloSocket.joinGroup(defaultHelloAddress.getAddress());

		InitStage(mainWindowStage);
		
		new Thread(new ListenServer(this, myAddress)).start();
		new Thread(new HelloListenServer(this, helloSocket)).start();
		
		mainWindowController.ChangeName();
	}
	
	private void InitStage(Stage mainWindowStage) throws Exception {
		this.mainWindowStage = mainWindowStage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		Parent mainWindowParent = fxmlLoader.load();
		mainWindowController = (MainWindowController)fxmlLoader.getController();
		mainWindowController.mainWindow = this;
		contactList = mainWindowController.contactList;
		this.mainWindowStage.setScene(new Scene(mainWindowParent));
		this.mainWindowStage.show();
	}

	public static void main(String[] args) {
		MainWindow.launch(args);
	}

}
