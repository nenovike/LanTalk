package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import com.nenovike.lanTalk.servers.MulticastListenServer;
import com.nenovike.lanTalk.servers.UnicastListenServer;
import com.nenovike.lanTalk.util.MessagePacket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
	String userName;
	Stage mainWindowStage;
	public MainWindowController mainWindowController;

	InetSocketAddress defaultHelloAddress;
	InetSocketAddress userAddress;

	MulticastSocket multicastSocket;
	DatagramSocket datagramSocket;
	UnicastListenServer listenServer;
	MulticastListenServer helloListenServer;
	Thread listenServerThread;
	Thread helloListenServerThread;

	private int defaultPort = 1234;

	boolean debug = true;

	@Override
	public void start(Stage mainWindowStage) throws IOException, InterruptedException {
		initStage(mainWindowStage);

		prepareSockets();
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
		userName = "LanTalk";
		defaultHelloAddress = new InetSocketAddress("233.0.0.0", 1111);
		int port = debug ? mainWindowController.getNewPort() : defaultPort;
		InetAddress address = mainWindowController.getNewAddress();
		userAddress = new InetSocketAddress(address, port);
		multicastSocket = new MulticastSocket(1111);
		multicastSocket.joinGroup(defaultHelloAddress.getAddress());
		datagramSocket = new DatagramSocket(userAddress);
	}

	private void initStage(Stage stage) throws IOException {
		mainWindowStage = stage;
		mainWindowStage.setOnCloseRequest(e -> {
			if (mainWindowController.conversationWindowStage != null)
				mainWindowController.conversationWindowStage.close();
		});
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		Parent mainWindowParent = fxmlLoader.load();
		mainWindowController = (MainWindowController) fxmlLoader.getController();
		mainWindowController.mainWindow = this;
		mainWindowStage.setScene(new Scene(mainWindowParent));
		mainWindowStage.show();
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

	public MainWindowController getController() {
		return mainWindowController;
	}

	public InetSocketAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(InetSocketAddress userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void addContactFromMessage(String message) {
		getController().addAddressToContactList(message);
	}
}
