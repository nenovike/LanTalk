package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
	String name = "LanTalk";
	Stage mainWindowStage;
	MainWindowController mainWindowController;
	
	InetSocketAddress defaultHelloAddress;
	InetSocketAddress myAddress;
	MulticastSocket multicastSocket;
	DatagramSocket datagramSocket;
	UnicastListenServer listenServer;
	MulticastListenServer helloListenServer;
	Thread listenServerThread;
	Thread helloListenServerThread;

	@Override
	public void start(Stage mainWindowStage) throws IOException, InterruptedException {
		prepareSockets();
		initStage(mainWindowStage);
		startServers();
		mainWindowController.changeName();
	}

	@Override
	public void stop() throws InterruptedException {
		listenServer.terminate();
		helloListenServer.terminate();
		multicastSocket.close();
		datagramSocket.close();
		listenServerThread.join();
		helloListenServerThread.join();
	}

	private void prepareSockets() throws IOException {
		defaultHelloAddress = new InetSocketAddress("233.0.0.0", 1111);
		myAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 1231);
		multicastSocket = new MulticastSocket(1111);
		multicastSocket.joinGroup(defaultHelloAddress.getAddress());
		datagramSocket = new DatagramSocket(myAddress);
	}

	private void initStage(Stage mainWindowStage) throws IOException {
		this.mainWindowStage = mainWindowStage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		Parent mainWindowParent = fxmlLoader.load();
		mainWindowController = (MainWindowController) fxmlLoader.getController();
		mainWindowController.mainWindow = this;
		this.mainWindowStage.setScene(new Scene(mainWindowParent));
		this.mainWindowStage.show();
	}

	private void startServers() {
		listenServer = new UnicastListenServer(this, datagramSocket);
		listenServerThread = new Thread(listenServer);
		listenServerThread.start();
		helloListenServer = new MulticastListenServer(this, multicastSocket);
		helloListenServerThread = new Thread(helloListenServer);
		helloListenServerThread.start();
	}

	public static void main(String[] args) {
		MainWindow.launch(args);
	}
}
